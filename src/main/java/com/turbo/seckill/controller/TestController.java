package com.turbo.seckill.controller;

import com.turbo.seckill.config.RequestRateLimitAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestRateLimitAnnotation(limitNum = 2, timeout = 4)
    @GetMapping("/rate_limit")
    public String testRateLimit() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 测试代码
         */
        return "success";
    }
}