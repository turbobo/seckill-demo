package com.turbo.seckill.vo;

import com.turbo.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Jusven
 * @Date 2021/8/1 18:57
 *
 * 订单详情返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private Order order;

    private GoodsVo goodsVo;
}
