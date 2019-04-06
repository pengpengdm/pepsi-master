package com.pepsi.rabbitmq.ttl;

import com.pepsi.rabbitmq.simple.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/08
 * describe:
 */
@Component
public class ReceiverMsg {

    private static final Logger log = LoggerFactory.getLogger(SendTTLMsg.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Value("${java.rabbitmq.consumer.service.retry.exchange}")
    private String userServiceListenerRetryExchange;

    @Value("${java.rabbitmq.consumer.service.fail.exchange}")
    private String userServiceListenerFailExchange;

    @Value("${java.rabbitmq.consumer.service.user.retry.routingkey}")
    private String userSerivceRetryOrFailRoutingKey;

    @SuppressWarnings("unused")
    @RabbitListener(queues = {"material@user"})
    public void consumerMessage(Message message, Channel channel) throws IOException {
        try {

//            messageHander.HandlerMessage(message, "user");
            /** 手动抛出异常,测试消息重试 */
            int i = 5 / 0;
        } catch (Exception e) {
            long retryCount = RabbitMqUtil.getRetryCount(message.getMessageProperties());
            CorrelationData correlationData = new CorrelationData(message.getMessageProperties().getCorrelationIdString());
            Message newMessage = null;
            if (retryCount >= 3) {
                /** 如果重试次数大于3,则将消息发送到失败队列等待人工处理 */
                newMessage = RabbitMqUtil.buildMessage(message);
                try {
                    rabbitTemplate.convertAndSend(userServiceListenerFailExchange,
                            userSerivceRetryOrFailRoutingKey, newMessage, correlationData);
                    log.info("用户体系服务消费者消费消息在重试3次后依然失败，将消息发送到fail队列,发送消息:" + new String(newMessage.getBody()));
                } catch (Exception e1) {
                    log.error("用户体系服务消息在发送到fail队列的时候报错:" + e1.getMessage() + ",原始消息:"
                            + new String(newMessage.getBody()));
                }
            } else {
                newMessage = RabbitMqUtil.buildMessage2(message);
                try {
                    /** 如果当前消息被重试的次数小于3,则将消息发送到重试队列,等待重新被消费{延迟消费} */
                    rabbitTemplate.convertAndSend(userServiceListenerRetryExchange,
                            userSerivceRetryOrFailRoutingKey, newMessage, correlationData);
                    log.info("用户服务消费者消费失败，消息发送到重试队列;" + "原始消息：" + new String(newMessage.getBody()) + ";第"
                            + (retryCount + 1) + "次重试");
                } catch (Exception e1) {
                    // 如果消息在重发的时候,出现了问题,可用nack,经过开发中的实际测试，当消息回滚到消息队列时，
                    // 这条消息不会回到队列尾部，而是仍是在队列头部，这时消费者会立马又接收到这条消息，进行处理，接着抛出异常，
                    // 进行回滚，如此反复进行。这种情况会导致消息队列处理出现阻塞，消息堆积，导致正常消息也无法运行
                    // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                    // 改为重新发送消息,经过多次重试后，如果重试次数大于3,就不会再走这，直接丢到了fail queue等待人工处理
                    log.error("消息发送到重试队列的时候，异常了:" + e1.getMessage() + ",重新发送消息");
                }
            }
        } finally {
            /**
             * 关闭rabbitmq的自动ack,改为手动ack 1、因为自动ack的话，其实不管是否成功消费了，rmq都会在收到消息后立即返给生产者ack,但是很有可能 这条消息我并没有成功消费
             * 2、无论消费成功还是消费失败,都要手动进行ack,因为即使消费失败了,也已经将消息重新投递到重试队列或者失败队列
             * 如果不进行ack,生产者在超时后会进行消息重发,如果消费者依然不能处理，则会存在死循环
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
