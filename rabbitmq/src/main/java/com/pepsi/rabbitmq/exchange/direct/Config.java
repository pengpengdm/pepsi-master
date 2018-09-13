package com.pepsi.rabbitmq.exchange.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/11
 * describe:
 */
@Profile("direct")
@Configuration
public class Config {

    @PostConstruct
    public  void pepsi() {
        System.out.println("初始化 direct Config");
    }

    @Bean
    public Queue queue() {
        String quque = "direct.quque";
        return new Queue(quque);
    }

    @Bean
    public DirectExchange exchange() {
        String exchange = "direct.exchange";
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bind(DirectExchange exchange,Queue queue) {
        String routeKey = "direct";
        return BindingBuilder.bind(queue).to(exchange).with(routeKey);
    }


    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public Sender sender() {
        return new Sender();
    }


}