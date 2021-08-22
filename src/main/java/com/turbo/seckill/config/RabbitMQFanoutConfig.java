//package com.turbo.seckill.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * RabbitMQ配置类--交换机模式
// * 生产者------队列-------消费者
// *
// * @Author Jusven
// * @Date 2021/8/5 20:11
// */
//@Configuration
//public class RabbitFanoutConfig {
//    private static final String QUEUE01 = "queue_fanout01";
//    private static final String QUEUE02 = "queue_fanout02";
//    private static final String EXCHANGE = "fanoutExchange";
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
//     * 交换机
//     *
//     * @return
//     */
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(EXCHANGE);
//    }
//
//    /**
//     * 绑定
//     */
//    @Bean
//    public Binding binding01() {
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//}
