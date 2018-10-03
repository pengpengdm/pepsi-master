package com.pepsi.rabbitmq.exchange.pepsi;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/17
 * describe:
 */
public class Sender {

    @Autowired
    private RabbitTemplate template;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send(){
        String json = "{\"idpUserId\":\"JKDKDK\",\"vin\":\"LSGNB83L0JA019043\"}";
        template.convertAndSend("pepsi.rabbit.exchange.test01", "pepsi.route.test01", json.getBytes(),message -> {
                        message.getMessageProperties().setHeader("jobName", "soft engineer");
                        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                        return message;
        });
        System.out.println("pepsi is send to mq!!!");
    }
}
