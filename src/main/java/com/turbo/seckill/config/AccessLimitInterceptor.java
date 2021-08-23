package com.turbo.seckill.config;

import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.utils.CookieUtil;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author Jusven
 * @Date 2021/8/22 21:15
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 需要将拦截器放到 MVC中，webconfig中
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {  //是一个方法
            User user = getUser(request, response);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == accessLimit) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String uri = request.getRequestURI();
            if (needLogin) {
                if (null == user) {
                    render(response, ResponseBeanEnum.LOGIN_ERROR);
                    return false;
                }
                uri += ":" + user.getId();
            }
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(uri);
            if (null == count) {
                //初始化
                valueOperations.set(uri,1,seconds, TimeUnit.SECONDS);
            }else if(count<maxCount){  //时间段内请求还未达到最大值
                valueOperations.increment(uri);
            }else{
                render(response, ResponseBeanEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;

    }

    private void render(HttpServletResponse response, ResponseBeanEnum responseBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        ResponseBean responseBean = ResponseBean.error(responseBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(responseBean));
        out.flush();
        out.close();
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);
    }
}
