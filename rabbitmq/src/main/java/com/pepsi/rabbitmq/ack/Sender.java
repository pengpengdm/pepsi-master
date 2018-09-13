package com.pepsi.rabbitmq.ack;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/13
 * describe:
 */

public class Sender implements RabbitTemplate.ReturnCallback/*,RabbitTemplate.ConfirmCallback*/{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1000,initialDelay = 500)
    public void send(){
        String msg = "你好现在是 " + new Date() +"";
        rabbitTemplate.setReturnCallback(this);
//        rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.convertSendAndReceive("pepsi.direct.exchange","direct",msg);
    }

    /*@Override
    public void confirm(CorrelationData correlationData, boolean ack, String msg) {
        if(!ack){
            System.out.println("HelloSender消息发送失败" + msg + correlationData.toString());
        }else {
            System.out.println("HelloSender 消息发送成功,msg="+msg);
        }
    }*/

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("sender return success" + message.toString()+"==="+replyCode+"==="+replyText+"==="+routingKey);
    }
}
