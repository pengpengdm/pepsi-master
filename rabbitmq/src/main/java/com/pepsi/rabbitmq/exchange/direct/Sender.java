package com.pepsi.rabbitmq.exchange.direct;

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
        String msg = "Pepsi send msg for mac";
        template.convertAndSend("direct.exchange","direct",msg);
        System.out.println(">>>>>>>>>>>>> send msg to mq");
    }
}
