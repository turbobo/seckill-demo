package com.turbo.seckill.config;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.utils.CookieUtil;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/***
 * aop拦截器
 */
@Slf4j
@Aspect
@Component
public class RequestRateLimitAspect {

    @Autowired
    private IUserService userService;

    /**
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    private Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    // 拦截带RequestRateLimitAnnotation注解的接口
    @Pointcut("@annotation(com.turbo.seckill.config.RequestRateLimitAnnotation)")
    public void execMethod() {}

    @Around("execMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取请求uri
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String reqUrl=request.getRequestURI();


        // 获取令牌
        RequestRateLimitAnnotation rateLimiter = this.getRequestRateLimiter(joinPoint);
        //判断用户是否登录
        User user = getUser(request, response);
        boolean needLogin = rateLimiter.needLogin();
        if (needLogin) {
            if (null == user) {
                return ResponseBean.error(ResponseBeanEnum.SESSION_ERROR);
            }
        }
        if (Objects.nonNull(rateLimiter)) {
            RateLimiter limiter = getRateLimiter(reqUrl, rateLimiter);
            // 请求获取令牌，参数为等待超时时间 （固定时间内未获取到则说明令牌不够）
            boolean acquire = limiter.tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit());
            if (!acquire) {
                return ResponseBean.error(ResponseBeanEnum.FREQUENT_REQUESTS);
//                return new Response(200, reqUrl.concat(rateLimiter.errMsg())).toString();
            }
        }

        //获得令牌，继续执行
        return joinPoint.proceed();
    }


    /**
     * 获取RateLimiter
     * @return
     */
    private RateLimiter getRateLimiter(String reqUrl, RequestRateLimitAnnotation rateLimiter) {
        RateLimiter limiter = limitMap.get(reqUrl);
        if (Objects.isNull(limiter)) {
            synchronized (this) {
                limiter = limitMap.get(reqUrl);
                if (Objects.isNull(limiter)) {
                    // 创建一个限流器，参数代表每秒生成的令牌数
                    // 例如：1秒钟生成1个令牌，也就是1秒中允许一个人访问
                    limiter = RateLimiter.create(rateLimiter.limitNum());
                    limitMap.put(reqUrl, limiter);
                    log.info("RequestRateLimitAspect请求{},创建令牌桶,容量{} 成功!!!", reqUrl, rateLimiter.limitNum());
                }
            }
        }
        return limiter;
    }

    /**
     * 获取注解对象
     * @param joinPoint 对象
     * @return ten LogAnnotation
     */
    private RequestRateLimitAnnotation getRequestRateLimiter(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (!StringUtils.isEmpty(name)) {
            for (Method method : methods) {
                RequestRateLimitAnnotation annotation = method.getAnnotation(RequestRateLimitAnnotation.class);
                if (!Objects.isNull(annotation) && name.equals(method.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (org.thymeleaf.util.StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);
    }
}