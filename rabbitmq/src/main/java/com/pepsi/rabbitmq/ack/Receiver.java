package com.pepsi.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.Date;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/13
 * describe:
 */
@RabbitListener(queues = "pepsi.direct.quque")
public class Receiver {

    @RabbitHandler
    public  void process(String msg, Channel channel, Message message) throws IOException {
        System.out.println("Receiver收到:" + msg +",收到时间="+new Date());
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            System.out.println("receiver success");
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            System.out.println("receiver fail");
        }

    }
}
