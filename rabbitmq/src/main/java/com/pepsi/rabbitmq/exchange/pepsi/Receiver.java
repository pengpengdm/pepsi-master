package com.pepsi.rabbitmq.exchange.pepsi;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/17
 * describe:
 */


@RabbitListener(queues="pepsi.rabbit.queue.test01")
public class Receiver {

    @RabbitHandler
    public void process(String msg){
        System.out.println(msg);
    }
}
