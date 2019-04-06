package com.pepsi.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/10/17
 * describe:
 */
public class SimpleSendCase {
    private static String queueName = "com.pepsi.simple.queue";
    private static String exchangeName="com.pepsi.simple.exchange";
    private static String routingKey="rbt.direct";


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("118.24.100.168");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queueName,true, false, false, null);
        channel.queueBind(queueName,exchangeName,routingKey);
        String message = "RabbitMQ Demo Test:" + System.currentTimeMillis();
        channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.close();
        connection.close();
    }

}
