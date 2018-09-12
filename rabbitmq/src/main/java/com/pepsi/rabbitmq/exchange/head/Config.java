package com.pepsi.rabbitmq.exchange.head;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/12
 * describe:
 */
@Profile("head")
@Configuration
public class Config {

    @Bean
    public HeadersExchange exchange(){
        String exchange="head.exchange";
        return new HeadersExchange(exchange);
    }

    @Bean
    public Queue queue03(){
        return new AnonymousQueue();
    }

    @Bean
    public Queue queue04(){
        return new AnonymousQueue();
    }

    @Bean
    public Binding bind01(HeadersExchange exchange,Queue queue03){
        return BindingBuilder.bind(queue03).to(exchange).where("queue03").matches("queue03");
    }
    @Bean
    public Binding bind02(HeadersExchange exchange,Queue queue04){
        return BindingBuilder.bind(queue04).to(exchange).where("queue04").matches("queue04");
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
