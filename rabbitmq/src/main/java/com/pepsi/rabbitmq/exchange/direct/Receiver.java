package com.pepsi.rabbitmq.exchange.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/12
 * describe:
 */
@RabbitListener(queues = "direct.quque")
public class Receiver {

    @RabbitHandler
    public void handleMsg(String msg){
        System.out.println("queue=direct.quque,exchange = ,msg="+msg);
    }
}
