package com.turbo.seckill.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀消息
 * @Author Jusven
 * @Date 2021/8/11 21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillMessage {
    private User user;
    private Long goodsId;
}
