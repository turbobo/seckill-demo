package com.turbo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turbo.seckill.pojo.SeckillOrder;
import com.turbo.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     *
     * @param user
     * @param goodsId
     * @return  orderId： 成功  -1：失败  0：排队中
     */
    Long getResult(User user, String goodsId);
}
