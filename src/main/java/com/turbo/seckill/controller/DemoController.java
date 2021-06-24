package com.turbo.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Jusven
 * @Date 2021/6/7 22:15
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    /**
     * 测试跳转
     * @param model
     * @return
     */
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name","jack");
        return "hello";
    }
}
