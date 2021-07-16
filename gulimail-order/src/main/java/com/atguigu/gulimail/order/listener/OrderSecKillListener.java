package com.atguigu.gulimail.order.listener;

import com.atguigu.common.to.mq.SeckillOrderTo;
import com.atguigu.gulimail.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author shkstart
 * @create 2021-07-05 17:05
 */
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSecKillListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void createOrder(SeckillOrderTo orderTo, Message message, Channel channel) throws IOException {
        System.out.println("***********接收到秒杀消息");
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            orderService.createSeckillOrder(orderTo);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicReject(deliveryTag,true);
        }
    }
}
