//package com.turbo.seckill.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * RabbitMQ配置类--直连
// * 生产者------队列-------消费者
// *
// * @Author Jusven
// * @Date 2021/8/5 20:11
// */
//@Configuration
//public class RabbitDirectConfig {
//    private static final String QUEUE01 = "queue_direct01";
//    private static final String QUEUE02 = "queue_direct02";
//    private static final String EXCHANGE = "directExchange";
//    private static final String ROUTINGKEY01 = "direct.red";
//    private static final String ROUTINGKEY02 = "direct.green";
//
//    @Bean
//    public Queue queue() {
//        return new Queue("queue", true);   //true表示队列持久化
//    }
//
//    /**
//     * 两个队列
//     *
//     * @return
//     */
//    @Bean
//    public Queue queue01() {
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02() {
//        return new Queue(QUEUE02);
//    }
//
//    /**
//     * 路由器
//     *
//     * @return
//     */
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(EXCHANGE);
//    }
//
//    /**
//     * 绑定
//     */
//    @Bean
//    public Binding binding01() {
//        //绑定给队列1，发给路由器，用路由Key01
//        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
//    }
//}
