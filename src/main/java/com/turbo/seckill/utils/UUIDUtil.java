package com.turbo.seckill.utils;

import java.util.UUID;

/**
 * @Author Jusven
 * @Date 2021/6/17 22:48
 *
 * uuid工具类
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
