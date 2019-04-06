package com.pepsi.rabbitmq.ttl;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/08
 * describe:
 */
@Configuration
public class Config {


    /**
     * 死信队列 交换机标识符
     */
    private static final String DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

    /**
     * 死信队列交换机绑定键标识符
     */
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 死信队列里面消息的超时时间
     */
    private static final String X_MESSAGE_TTL = "x-message-ttl";

    /**
     * 声明交换机,支持持久化.
     * rabbitmq常用几种exchange,比如direct, fanout, topic,可根据具体业务需求配置
     * 命名规范参考 scm3.services,scm3.services.retry,scm3.services.failed
     * @return the exchange
     */
    @Bean("scm3.materials")
    public Exchange directExchange() {
        //.durable(true) exchange的持久化
        return ExchangeBuilder.directExchange("scm3.materials").durable(true).build();
    }
    @Bean("scm3.materials.retry")
    public Exchange retryDirectExchange() {
        return ExchangeBuilder.directExchange("scm3.materials.retry").durable(true).build();
    }
    @Bean("scm3.materials.fail")
    public Exchange failDirectExchange() {
        return ExchangeBuilder.directExchange("scm3.materials.fail").durable(true).build();
    }
    /**
     * ##########################################供需关系服务-声明queue#####################################################
     */
    /**
     * 声明一个队列 .{供需关系主队列} 队列名称参考 【服务名称】@订阅服务标识 如
     * material@供需关系,material@供需关系@retry,material@供需关系@failed
     * material@采购计划,material@采购计划@retry,@material@采购计划@failed
     *
     * @return the queue
     */
    @Bean("material@supply")
    public Queue directQueue() {
        return QueueBuilder.durable("material@supply").build();
    }
    /**
     * 供需关系 重试队列
     *
     * @return
     */
    @Bean("material@supply@retry")
    public Queue retryDirectQueue() {
        Map<String, Object> args = new ConcurrentHashMap<>(3);
        // 将消息重新投递到exchange中
        args.put(DEAD_LETTER_EXCHANGE, "scm3.materials");
        args.put(DEAD_LETTER_ROUTING_KEY, "material@supply");
        //在队列中延迟30s后，消息重新投递到x-dead-letter-exchage对应的队列中,routingkey是自己配置的
        args.put(X_MESSAGE_TTL, 30 * 1000);
        return QueueBuilder.durable("material@supply@retry").withArguments(args).build();
    }
    /**
     * 供需关系 失败队列
     *
     * @return
     */
    @Bean("material@supply@failed")
    public Queue failDirectQueue() {
        return QueueBuilder.durable("material@supply@failed").build();
    }
    /**
     * ###########################################供需关系结束###############################################
     */


    /** ########################################用户服务开始############################################ */
    /**
     * @return the queue
     */
    @Bean("material@user")
    public Queue userDirectQueue() {
        return QueueBuilder.durable("material@user").build();
    }
    /**
     * 用户服务 重试队列
     *
     * @return
     */
    @Bean("material@user@retry")
    public Queue userRetryDirectQueue() {
        Map<String, Object> args = new ConcurrentHashMap<>(3);
        args.put(DEAD_LETTER_EXCHANGE, "scm3.materials");
        args.put(DEAD_LETTER_ROUTING_KEY, "material@user");
        args.put(X_MESSAGE_TTL, 30 * 1000);
        return QueueBuilder.durable("material@user@retry").withArguments(args).build();
    }
    /**
     * 供需关系 失败队列
     *
     * @return
     */
    @Bean("material@user@failed")
    public Queue userFailDirectQueue() {
        return QueueBuilder.durable("material@user@failed").build();
    }
    /** #####################################用户服务结束################################################ */
    /**
     * 以下是消费者需要处理的 通过绑定键(rounting key) 将指定队列绑定到一个指定的交换机 .要求该消息与一个特定的路由键完全匹配
     * @param queue the queue
     * @param exchange the exchange
     * @return the binding
     */
    /**
     * ######################################供需关系绑定###################################################
     */
    @Bean
    public Binding directBinding(@Qualifier("material@supply") Queue queue,
                                 @Qualifier("scm3.materials") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("direct_rounting_key").noargs();
    }
    @Bean
    public Binding directQueueBinding(@Qualifier("material@supply") Queue queue,
                                      @Qualifier("scm3.materials") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@supply").noargs();
    }
    @Bean
    public Binding retryDirectBinding(@Qualifier("material@supply@retry") Queue queue,
                                      @Qualifier("scm3.materials.retry") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@supply").noargs();
    }
    @Bean
    public Binding failDirectBinding(@Qualifier("material@supply@failed") Queue queue,
                                     @Qualifier("scm3.materials.fail") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@supply").noargs();
    }
    /**
     * ######################################用户服务绑定###################################################
     */
    @Bean
    public Binding userDirectBinding(@Qualifier("material@user") Queue queue,
                                     @Qualifier("scm3.materials") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("direct_rounting_key").noargs();
    }
    @Bean
    public Binding userDirectQueueBinding(@Qualifier("material@user") Queue queue,
                                          @Qualifier("scm3.materials") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@user").noargs();
    }
    @Bean
    public Binding userRetryDirectBinding(@Qualifier("material@user@retry") Queue queue,
                                          @Qualifier("scm3.materials.retry") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@user").noargs();
    }
    @Bean
    public Binding userFailDirectBinding(@Qualifier("material@user@failed") Queue queue,
                                         @Qualifier("scm3.materials.fail") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("material@user").noargs();
    }


}
