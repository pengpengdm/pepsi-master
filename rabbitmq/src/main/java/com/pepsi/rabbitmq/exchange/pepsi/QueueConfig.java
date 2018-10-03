package com.pepsi.rabbitmq.exchange.pepsi;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/14
 * describe:
 */
@Profile("onstar")
@Configuration
public class QueueConfig {

    @Bean
    public Queue mobileUserQueue() {
        return new Queue("pepsi.rabbit.queue.test01");
    }

    @Bean
    public TopicExchange mobileUserExchange(){
        return new TopicExchange("pepsi.rabbit.exchange.test01");
    }

    @Bean
    public Binding mobileUserBinding(Queue mobileUserQueue,TopicExchange mobileUserExchange) {
        Binding binding = BindingBuilder.bind(mobileUserQueue).to(mobileUserExchange).with("pepsi.route.test01");
        return binding;
    }

    @Bean
    public Receiver receiver(){
        return new Receiver();
    }


    @Bean
    public Sender sender(){
        return new Sender();
    }

}
