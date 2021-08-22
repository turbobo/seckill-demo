package com.turbo.seckill.controller;


import com.turbo.seckill.pojo.User;
import com.turbo.seckill.rabbitmq.MQSender;
import com.turbo.seckill.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private MQSender mqSender;

//    /**
//     * 用户信息--测试
//     * @param user
//     * @return
//     */
//    @RequestMapping("/info")
//    @ResponseBody
//    public ResponseBean info(User user){
//        return ResponseBean.success(user);
//    }
//
//    /**
//     * 测试发送Rabbit消息
//     * http://localhost:8080/user/mq
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello mq!");
//    }
//
//    /**
//     * 发送消息到交换机  fanout模式
//     */
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq_fanout(){
//        mqSender.send("hello mq fanout!");
//    }
//
//    /**
//     * Direct模式
//     */
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq_direct01(){
//        mqSender.send01("hello red direct01!");
//    }
//
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq_direct02(){
//        mqSender.send02("hello green direct02!");
//    }
//
//    @RequestMapping("/mq/topic01")
//    @ResponseBody
//    public void mq_topic01(){
//        mqSender.send03("hello  red!");
//    }
//
//    @RequestMapping("/mq/topic02")
//    @ResponseBody
//    public void mq_topic02(){
//        mqSender.send04("hello  green!");
//    }
//
//    @RequestMapping("/mq/headers01")
//    @ResponseBody
//    public void mq_headers01(){
//        mqSender.send07("hello  headers01!");
//    }
//
//    @RequestMapping("/mq/headers02")
//    @ResponseBody
//    public void mq_headers02(){
//        mqSender.send08("hello  headers02!");
//    }

}
