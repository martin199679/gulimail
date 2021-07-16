package com.atguigu.gulimail.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author shkstart
 * @create 2021-07-01 20:10
 */
@Configuration
public class MyMQConfig {
    @Bean
    public Exchange orderEventExchange() {

        return new TopicExchange("order-event-exchange", // name
                true,// durable
                false); // autoDelete
        // 还有一个参数是Map<String, Object> arguments
    }

    /**
     * 延迟队列
     */
    @Bean
    public Queue orderDelayQueue() {
        HashMap<String, Object> arguments = new HashMap<>();
        //死信交换机
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        //死信路由键
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000); // 消息过期时间 1分钟
        return new Queue("order.delay.queue", // 队列名字
                true, //是否持久化
                false, // 是否排他
                false, // 是否自动删除
                arguments); // 属性map
    }

    /**
     * 普通队列
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;
    }

    /**
     * 创建订单的binding
     */
    @Bean
    public Binding orderCreateBinding() {

        return new Binding("order.delay.queue", // 目的地（队列名或者交换机名字）
                Binding.DestinationType.QUEUE,  // 目的地类型（Queue、Exhcange）
                "order-event-exchange", // 交换器
                "order.create.order", // 路由key
                null); // 参数map
    }

    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    @Bean
    public Binding orderReleaseOrderBinding() {
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }

    /**
     * 商品秒杀队列
     */
    @Bean
    public Queue orderSecKillOrrderQueue() {
        Queue queue = new Queue("order.seckill.order.queue", true, false, false);
        return queue;
    }

    @Bean
    public Binding orderSecKillOrrderQueueBinding() {
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        // 			Map<String, Object> arguments
        Binding binding = new Binding(
                "order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                null);

        return binding;
    }
}
