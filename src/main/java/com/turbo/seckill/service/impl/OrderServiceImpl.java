package com.turbo.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.turbo.seckill.exception.GlobalException;
import com.turbo.seckill.mapper.OrderMapper;
import com.turbo.seckill.pojo.Order;
import com.turbo.seckill.pojo.SeckillGoods;
import com.turbo.seckill.pojo.SeckillOrder;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IGoodsService;
import com.turbo.seckill.service.IOrderService;
import com.turbo.seckill.service.ISeckillGoodsService;
import com.turbo.seckill.service.ISeckillOrderService;
import com.turbo.seckill.utils.MD5Util;
import com.turbo.seckill.utils.UUIDUtil;
import com.turbo.seckill.vo.GoodsVo;
import com.turbo.seckill.vo.OrderDetailVo;
import com.turbo.seckill.vo.ResponseBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author turbo
 * @since 2021-06-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀订单
     *
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //去数据库查询库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        //秒杀商品表减库存
        //        seckillGoodsService.updateById(seckillGoods);

        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        boolean seckillGoodsFlag = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count", seckillGoods.getStockCount())
//                .eq("id", seckillGoods.getId()).gt("stock_count", 0));


        boolean seckillGoodsFlag = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql(
                "stock_count = stock_count - 1").eq("goods_id", goods.getId()).gt("stock_count", 0));
        if (!seckillGoodsFlag) {
            return null;
        }
/*        if(seckillGoods.getStockCount() < 1){
            return null;
        }*/

        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty" + goods.getId(), "0");
            return null;
        }

        //生成订单
        Order order = new Order();
//        order.setId(goods.getId());
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
//        order.setPayDate();
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
//        seckillOrder.setId(order.getId());   //插入自动生成主键
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
        return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(ResponseBeanEnum.ORDER_NOT_EXISTS);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);
        return detailVo;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "12345");
        //把路径信息str存到redis中，用user  goodsId作key
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        //判断redis中的路径（初次生成的路径，存到redis中的）和页面秒杀提交的路径是否相同
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    @Override
    public boolean checkCpatcha(User user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        //和redis中的验证码比较
        String redisCpatcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return redisCpatcha.equals(captcha);
    }
}
