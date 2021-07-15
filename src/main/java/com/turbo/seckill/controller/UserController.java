package com.turbo.seckill.controller;


import com.turbo.seckill.pojo.User;
import com.turbo.seckill.vo.ResponseBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author turbo
 * @since 2021-06-08
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 用户信息--测试
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public ResponseBean info(User user){
        return ResponseBean.success(user);
    }
}
