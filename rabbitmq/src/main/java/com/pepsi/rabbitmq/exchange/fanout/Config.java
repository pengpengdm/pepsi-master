package com.pepsi.rabbitmq.exchange.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/12
 * describe:
 */
@Profile("fanout")
@Configuration
public class Config {

    @PostConstruct
    public  void pepsi(){
        System.out.println("初始化 fanout Config");
    }


    @Bean
    public FanoutExchange exchange(){
        String exchange = "fanout.exchange";
        return  new FanoutExchange(exchange);
    }


    @Bean
    public Queue queue01(){
        String quque01 = "fanout.quque01";
        return new Queue(quque01);
    }

    @Bean
    public Queue queue02(){
        String quque02 = "fanout.quque02";
        return new Queue(quque02);
    }

    @Bean
    public Binding bind01(FanoutExchange exchange, Queue queue01){
        return BindingBuilder.bind(queue01).to(exchange);
    }

    @Bean
    public Binding bind02(FanoutExchange exchange,Queue queue02){
        return BindingBuilder.bind(queue02).to(exchange);
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
