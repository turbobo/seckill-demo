package com.turbo.seckill.config;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;
/***
 * 请求速率限制注解类
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestRateLimitAnnotation {
    /**
     * 默认表示每秒产生几个令牌 （guava默认速率是以秒为单位，令牌桶最大容量就是 速率大小）
     * @return
     */
    int limitNum();
    /**
     * 获取令牌超时时间  （固定时间内未获取到则说明令牌不够）
     * @return
     */
    int timeout();
    /**
     * 单位-默认毫秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
    /**
     * 无法获取令牌时的错误信息
     * @return
     */
    String errMsg() default "请求太频繁！";

    boolean needLogin() default true;
}