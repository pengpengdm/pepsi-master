package com.pepsi.rabbitmq.ack;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/13
 * describe:
 */
@Configuration
@Profile("ack")
public class RbtmqConf {

    @Bean
    public Queue queue(){
        return new Queue("pepsi.direct.quque");
    }

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("pepsi.direct.exchange");
    }






//    @Bean
//    public Binding bind(DirectExchange exchange,Queue queue){
//        return BindingBuilder.bind(queue).to(exchange).with("direct");
//    }


    @Bean
    public Receiver receiver(){
        return new Receiver();
    }

    @Bean
    public Sender send(){
        return new Sender();
    }
}
