package com.pepsi.rabbitmq.exchange.head;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/12
 * describe:
 */
public class Receiver {

    @RabbitListener(queues = "#{queue03.name}")
    public void receive1(String in) {
        receive(in, 1);
    }

    @RabbitListener(queues = "#{queue04.name}")
    public void receive2(String in) {
        receive(in, 2);
    }

    public void receive(String in, int receiver) {
        System.out.println("instance " + receiver + " [x] Received '" + in + "'");
    }



}
