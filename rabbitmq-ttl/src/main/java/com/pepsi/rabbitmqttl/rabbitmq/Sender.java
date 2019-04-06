package com.pepsi.rabbitmqttl.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/28
 * describe:
 */
@Component
public class Sender {

    @Autowired
    private RabbitTemplate template;

    public void sendAsyn(){
        String receiveMessage = "{\"name\":\"pepsi\"}";
        Message message = MessageBuilder.withBody(receiveMessage.getBytes())
                .setContentType("application/json").build();
        template.convertAndSend("com.pepsi.pushExchange","routekey.delay",message);
    }

}
