package com.turbo.seckill.controller;

import com.turbo.seckill.config.LoginUser;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IGoodsService;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author Jusven
 * @Date 2021/6/18 11:18
 * 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    IUserService userService;
    @Autowired
    IGoodsService goodsService;

    /**
     * 商品列表
     * @param request
     * @param response
     * @param model
     * @param ticket
     * @return
     */
//    @RequestMapping("/toList")
//    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, @CookieValue("userTicket") String ticket){
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        //根据ticket获取user
//        /*//从session获取用户
//        User user = (User) session.getAttribute(ticket);*/
//
//        //从redis获取用户信息
//        User user = (User) userService.getUserByCookie(ticket,request,response);
//        //用户是否登录
//        if(null == user){
//            return "login";
//        }
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goodsService.findGoodsVo());
//        return "goodsList";
//    }

    /**
     * 自定义webconfig、参数解析器UserArgumentResolver
     * 商品列表  -- 获得用户信息之前已经判断
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("/toList")
    public String toList(Model model, @LoginUser User user){
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable Long goodsId){
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀还没开始
        if (nowDate.before(startDate)){
            remainSeconds = ((int) (startDate.getTime() - nowDate.getTime()))/1000;
        }else if (nowDate.after(endDate)){
            //秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("goods",goodsVo);
        return "goodsDetail";
    }

}
