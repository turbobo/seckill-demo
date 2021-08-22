package com.turbo.seckill.rabbitmq;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.turbo.seckill.pojo.SeckillMessage;
import com.turbo.seckill.pojo.SeckillOrder;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.service.IGoodsService;
import com.turbo.seckill.service.IOrderService;
import com.turbo.seckill.utils.JsonUtils;
import com.turbo.seckill.vo.GoodsVo;
import com.turbo.seckill.vo.ResponseBean;
import com.turbo.seckill.vo.ResponseBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 *
 * @Author Jusven
 * @Date 2021/8/5 20:22
 */
@Slf4j
@Service
public class MQReceiver {
    /*@RabbitListener(queues = "queue")
    public void receive(Object msg) {
        log.info("接收消息" + msg);
    }

    @RabbitListener(queues = "queue_fanout01")
    public void receive01(Object msg) {
        log.info("QUEUE01接收消息" + msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive02(Object msg) {
        log.info("QUEUE02接收消息" + msg);
    }

    @RabbitListener(queues = "queue_direct01")
    public void receive03(Object msg) {
        log.info("QUEUE01接收消息" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void receive04(Object msg) {
        log.info("QUEUE02接收消息" + msg);
    }

    @RabbitListener(queues = "queue_topic01")
    public void receive05(Object msg) {
        log.info("QUEUE01接收消息" + msg);
    }

    @RabbitListener(queues = "queue_topic02")
    public void receive06(Object msg) {
        log.info("QUEUE02接收消息" + msg);
    }

    @RabbitListener(queues = "queue_headers01")
    public void receive07(Message message) {
        log.info("QUEUE01接收Message对象" + message);
        log.info("QUEUE01接收消息" + new String(message.getBody()));
    }

    @RabbitListener(queues = "queue_headers02")
    public void receive08(Message message) {
        log.info("QUEUE02接收Message对象" + message);
        log.info("QUEUE02接收消息" + new String(message.getBody()));
    }*/
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtils.jsonToObject(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goodsVo.getStockCount() < 0) {
            return ;
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return ;
        }
        //下单操作
        orderService.seckill(user,goodsVo);
    }

}
