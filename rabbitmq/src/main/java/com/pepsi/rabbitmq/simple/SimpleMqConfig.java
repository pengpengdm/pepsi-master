package com.pepsi.rabbitmq.simple;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.Executors;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/10/18
 * describe:
 */
@Configuration
public class SimpleMqConfig {

    @Bean
    public Queue queue(){
        String queueName = "com.pepsi.simple.queue";
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange exchange(){
        String exchangeName="com.pepsi.simple.exchange";
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding bind(Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("direct");
    }

    @Bean
    public SimpleConsumerCase simpleConsumerCase(){
        return new SimpleConsumerCase();
    }

    @Bean
    public SimpleSendCase simpleReceiverCase(){
        return new SimpleSendCase();
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory, RabbitProperties properties) {
        cachingConnectionFactory.setBeanName("PEPSI");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setMandatory(determineMandatoryFlag(properties));
        RabbitProperties.Template templateProperties = properties.getTemplate();
        RabbitProperties.Retry retryProperties = templateProperties.getRetry();
        if (retryProperties.isEnabled()) {
            rabbitTemplate.setRetryTemplate(createRetryTemplate(retryProperties));
        }
        if (templateProperties.getReceiveTimeout() != null) {
            rabbitTemplate.setReceiveTimeout(templateProperties.getReceiveTimeout());
        }
        if (templateProperties.getReplyTimeout() != null) {
            rabbitTemplate.setReplyTimeout(templateProperties.getReplyTimeout());
        }
        return rabbitTemplate;
    }

    private boolean determineMandatoryFlag(RabbitProperties properties) {
        Boolean mandatory = properties.getTemplate().getMandatory();
        return (mandatory != null ? mandatory : properties.isPublisherReturns());
    }

    private RetryTemplate createRetryTemplate(RabbitProperties.Retry properties) {
        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(properties.getMaxAttempts());
        template.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(properties.getInitialInterval());
        backOffPolicy.setMultiplier(properties.getMultiplier());
        backOffPolicy.setMaxInterval(properties.getMaxInterval());
        template.setBackOffPolicy(backOffPolicy);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, CachingConnectionFactory cachingConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setTaskExecutor(Executors.newFixedThreadPool(30));
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); //设置确认模式手工确认, 默认是自动确认
        configurer.configure(factory, cachingConnectionFactory);
        return factory;
    }

    @Bean
    public RabbitAdmin admin(ConnectionFactory connFactory) {
        RabbitAdmin admin = new RabbitAdmin(connFactory);
        admin.setIgnoreDeclarationExceptions(true);
        return admin;

    }
}
