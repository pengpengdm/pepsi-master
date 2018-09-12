package com.pepsi.rabbitmq.exchange.head;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
        String msg = "{\"queue02\": \"queue02\",\"pepsi\": \"pepsi\"}";
        MessageProperties properties = new MessageProperties();
        properties.setHeader("queue03","queue03");
        Message message = new Message(msg.getBytes(),properties);
        template.convertAndSend("head.exchange","",message);
        System.out.println(" [x] Sent '" + msg + "'");
    }

}
