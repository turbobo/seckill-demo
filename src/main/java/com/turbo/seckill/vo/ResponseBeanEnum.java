package com.turbo.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Author Jusven
 * @Date 2021/6/12 21:41
 *  * 公共返回对象枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum ResponseBeanEnum {
    //通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),

    //登录模块 5002xx
    LOGIN_ERROR(500200,"用户名或密码错误"),
    MOBILE_ERROR(500201,"手机号码格式错误"),
    BIND_ERROR(500202,"参数校验异常"),

    //秒杀模块 5005xx
    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500501,"该商品每人限购一件")
    ;

    private long code;
    private String message;
}
