package com.pepsi.rabbitmqttl.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/28
 * describe:
 */
@Component
public class Receiver {

    @RabbitListener(queues = "com.pepsi.pushQueue")
    public void process(@Payload Object name){
        System.out.println(name.toString());

    }

}
