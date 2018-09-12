package com.pepsi.rabbitmq.exchange.topic;

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
@Profile("topic")
@Configuration
public class Config {

    @Bean
    public TopicExchange exchange() {
        String exchange="topic.exchange";
        return new TopicExchange(exchange);
    }

    @Bean
    public  Queue queue01(){
        return new AnonymousQueue();
    }

    @Bean
    public  Queue queue02(){
        return new AnonymousQueue();
    }

    @Bean
    public Binding bind01(Queue queue01,TopicExchange exchange){
        return BindingBuilder.bind(queue01).to(exchange).with("*.orange.*");
    }
    @Bean
    public Binding bind02(Queue queue01,TopicExchange exchange){
        return BindingBuilder.bind(queue01).to(exchange).with("*.*.rabbit");
    }
    @Bean
    public Binding bind03(Queue queue02,TopicExchange exchange){
        return BindingBuilder.bind(queue02).to(exchange).with("lazy.#");
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
