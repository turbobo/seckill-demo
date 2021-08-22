package com.turbo.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turbo.seckill.mapper.SeckillOrderMapper;
import com.turbo.seckill.pojo.SeckillOrder;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult(User user, String goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId()).eq("goods_id", goodsId));
        if (null != seckillOrder) {
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty" + goodsId)) {
            return -1L;
        } else {
            return 0L;
        }
    }
}
