package com.turbo.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.turbo.seckill.pojo.Order;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.vo.GoodsVo;
import com.turbo.seckill.vo.OrderDetailVo;

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

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 获取秒杀接口
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * 校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    boolean checkCpatcha(User user, Long goodsId, String captcha);
}
