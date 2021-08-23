package com.turbo.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Jusven
 * @Date 2021/8/22 21:11
 * 接口限流配置
 */
@Retention(RetentionPolicy.RUNTIME)   //运行时生效
@Target(ElementType.METHOD)     //针对方法生效
public @interface AccessLimit {
    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
