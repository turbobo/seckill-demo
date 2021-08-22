package com.turbo.seckill.vo;

import com.turbo.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.PrintStream;

/**
 * @Author Jusven
 * @Date 2021/7/25 22:49
 * 商品详情返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {
    private User user;

    private GoodsVo goodsVo;

    private int secKillStatus;

    private int remainSeconds;

}
