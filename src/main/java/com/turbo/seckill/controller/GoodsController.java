package com.turbo.seckill.controller;

import com.turbo.seckill.config.LoginUser;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IGoodsService;
import com.turbo.seckill.service.IUserService;
import com.turbo.seckill.vo.DetailVo;
import com.turbo.seckill.vo.GoodsVo;
import com.turbo.seckill.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

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
     *
     * windows 优化前：压测         30000次  qps：385.1    258.6
     * windows 整个页面缓存后压测   30000次  qps：385.1    841.5
     *
     * linux   优化前：压测  30000次  qps：496      462.1
     *
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, @LoginUser User user, HttpServletRequest request, HttpServletResponse response){
        //从redis直接获取页面，如果不为空，直接返回
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());

        //如果html为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            //存入redis,key为goodsList,60秒过期
            valueOperations.set("goodsList", html,60, TimeUnit.SECONDS);
        }
//        return "goodsList";
        return html;
    }

    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){
        //从redis直接获取页面，如果不为空，直接返回
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
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
        //如果html为空，手动渲染，存入redis并返回
        WebContext context = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",context);
        if(!StringUtils.isEmpty(html)){
            //存入redis,key为goodsList,60秒过期
            valueOperations.set("goodsDetail" + goodsId, html,60, TimeUnit.SECONDS);
        }
        return html;
//        return "goodsDetail";

    }

    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public ResponseBean toDetail(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response){
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
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);


        return ResponseBean.success(detailVo);
//        return "goodsDetail";

    }

}
