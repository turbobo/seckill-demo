package com.turbo.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 消息发送者
 *
 * @Author Jusven
 * @Date 2021/8/5 20:15
 */
@Slf4j
@Service
public class MQSender {
    @Autowired
    RabbitMessagingTemplate rabbitMessagingTemplate;

    /*public void send(Object msg) {
        log.info("发送消息" + msg);
        //往名为queue的队列发送msg
//        rabbitMessagingTemplate.convertAndSend("queue",msg);

        //消息发送到交换机
        rabbitMessagingTemplate.convertAndSend("fanoutExchange", "", msg);

    }

    public void send01(Object msg) {
        log.info("发送red消息：" + msg);
        //queue.red要和配置中的路由Key对应
        rabbitMessagingTemplate.convertAndSend("directExchange","queue.red",msg);
    }

    public void send02(Object msg) {
        log.info("发送green消息：" + msg);
        rabbitMessagingTemplate.convertAndSend("directExchange","queue.green",msg);
    }

    public void send03(Object msg) {
        log.info("发送red消息：" + msg);
        rabbitMessagingTemplate.convertAndSend("topicExchange","queue.red.message",msg);
    }

    public void send04(Object msg) {
        log.info("发送green消息(被两个queue接收)：" + msg);
        rabbitMessagingTemplate.convertAndSend("topicExchange","abc.queue.green.message",msg);
    }

    public void send07(String msg) {
        log.info("发送red消息：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","normal");
        Message message = new Message(msg.getBytes(),properties);
        rabbitMessagingTemplate.convertAndSend("headersExchange","",message);
    }

    public void send08(String msg) {
        log.info("发送green消息(被两个queue接收)：" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("color","red");
        properties.setHeader("speed","fast");
        Message message = new Message(msg.getBytes(),properties);
        rabbitMessagingTemplate.convertAndSend("headersExchange","",message);
    }*/

    /**
     * 发送秒杀消息
     * @param message
     */
    public void sendSeckillMessage(String message){
        log.info("发送秒杀消息：" + message);
        rabbitMessagingTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }

}
