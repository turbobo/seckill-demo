package com.turbo.seckill.config;

import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Jusven
 * @Date 2021/6/19 20:24
 * 自定义用户参数 解析器
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private IUserService userService;

    /**
     * 入参必须满足有自定义注解以及入参必须有Target对象，满足则放行返回true。
     * @param
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.getParameterType().isAssignableFrom(User.class) && parameter.hasParameterAnnotation(LoginUser.class)) {
//        if (parameter.getParameterType().isAssignableFrom(User.class)) {
            return true;
        }
        return false;
    }

/*    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;   //返回true才执行下面的方法
    }*/

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //通过线程获取用户
//        return UserContext.getUser();


        //已经通过拦截器获取用户
        //获取 cookie
        HttpServletRequest request =  nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if(StringUtils.isEmpty(ticket)){
            return null;
        }
        return userService.getUserByCookie(ticket,request,response);
    }
}
