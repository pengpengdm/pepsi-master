package com.pepsi.rabbitmqttl.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/28
 * describe:
 */
@Configuration
public class RabbitmqConfig {

    private String DEAD_LETTER_EXCHANGE="dlexchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "dlroutekey";
    private static final String X_MESSAGE_TTL = "x-message-ttl";



    @Bean
    public Exchange delayExchange(){
        //.durable(true) exchange的持久化
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    @Bean
    public Exchange pushExchange(){
        //.durable(true) exchange的持久化
        return ExchangeBuilder.directExchange("com.pepsi.pushExchange").durable(true).build();
    }



    @Bean
    public Queue delayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        arguments.put("x-message-ttl",30000);
        Queue queue = new Queue("com.pepsi.delayQueue",true,false,false,arguments);
        return queue;
    }

    @Bean
    public Queue pushQueue(){
        return  QueueBuilder.durable("com.pepsi.pushQueue").build();
    }


    @Bean
    public Binding bindingDelay(Exchange pushExchange,Queue delayQueue){
        return BindingBuilder.bind(delayQueue).to(pushExchange).with("routekey.delay").noargs();
    }

    @Bean
    public Binding bindingPush(Exchange delayExchange,Queue pushQueue){
        return BindingBuilder.bind(pushQueue).to(delayExchange).with(DEAD_LETTER_ROUTING_KEY).noargs();
    }



}
