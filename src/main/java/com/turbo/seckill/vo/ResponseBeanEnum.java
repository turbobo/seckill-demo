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
    MOBILE_NOT_EXIST(500203,"手机号存在"),
    PASSWORD_UPDATE_FAIL(500204,"密码更新失败"),
    SESSION_ERROR(500205,"用户不存在"),

    //秒杀模块 5005xx
    EMPTY_STOCK(500500,"库存不足"),
    REPEAT_ERROR(500501,"该商品每人限购一件"),
    REQUEST_ILLEGAL(500502,"请求非法，请重新尝试"),
    ERROR_CAPTCHA(500503,"验证码错误，请重新输入"),
    ACCESS_LIMIT_REACHED(500504,"访问过于频繁，请稍后重试"),

    //订单模块5003xx
    ORDER_NOT_EXISTS(500300,"订单信息不存在")
    ;

    private long code;
    private String message;
}
