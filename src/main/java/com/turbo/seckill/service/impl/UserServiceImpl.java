package com.turbo.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turbo.seckill.exception.GlobalException;
import com.turbo.seckill.mapper.UserMapper;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.utils.CookieUtil;
import com.turbo.seckill.utils.MD5Util;
import com.turbo.seckill.utils.UUIDUtil;
import com.turbo.seckill.vo.LoginVo;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author turbo
 * @since 2021-06-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 登录
     * @param loginVo
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //参数校验 ---> IsMobile注解判断
//        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
//            return ResponseBean.error(ResponseBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile)){
//            return ResponseBean.error(ResponseBeanEnum.MOBILE_ERROR);
//        }

        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if(null == user){
//            return ResponseBean.error(ResponseBeanEnum.LOGIN_ERROR);
            throw new GlobalException(ResponseBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否和数据库密码一致
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
//            return ResponseBean.error(ResponseBeanEnum.LOGIN_ERROR);
            throw new GlobalException(ResponseBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();

        //用户信息存入session
//        request.getSession().setAttribute(ticket,user);

        //用户信息存入redis, 加上user:前缀
        redisTemplate.opsForValue().set("user:" + ticket,user);

        //使用cookie记录uuid,以便取出session或者 redis
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        //把生成的 ticket放入封装类
        return ResponseBean.success(ticket);
    }


    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null != user){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public ResponseBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket,request,response);
        if(user == null){
            throw new GlobalException(ResponseBeanEnum.MOBILE_ERROR);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result = userMapper.updateById(user);
        //更新成功，删除redis
        if(1 == result){
            redisTemplate.delete("user" + userTicket);
            return ResponseBean.success();
        }
        return ResponseBean.error(ResponseBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
