package com.turbo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turbo.seckill.pojo.Order;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
public interface IOrderService extends IService<Order> {

    /**
     * 秒杀订单
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);
}
