package com.pepsi.rabbitmq.exchange.fanout;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/12
 * describe:
 */
public class Sender {

    @Autowired
    private RabbitTemplate template;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send(){
        String msg = "i am fanout exchange";
        template.convertAndSend("fanout.exchange","",msg);
        System.out.println(" [x] Sent '" + msg + "'");
    }
}
