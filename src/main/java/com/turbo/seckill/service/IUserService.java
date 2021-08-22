package com.turbo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.vo.LoginVo;
import com.turbo.seckill.vo.ResponseBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author turbo
 * @since 2021-06-08
 */
public interface IUserService extends IService<User> {
    ResponseBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @return
     */
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    /**
     * 更新用户密码
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    public ResponseBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
