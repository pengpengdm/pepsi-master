package com.pepsi.rabbitmq.ttl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/08
 * describe:
 */
@Component
public class SendTTLMsg{

    private static final Logger log = LoggerFactory.getLogger(ReceiverMsg.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${java.rabbitmq.send.service.exchange}")
    private String sendExchange;

    @Value("${java.rabbitmq.send.service.rountkey}")
    private String rountKey;
    /**
     * demo级别，先本地缓存,真正实现可考虑用redis 如果是放到redis中，有可能exchange一直不给生产者反馈{比如rabbitmq挂了,这种只能重启rabbitmq}
     * 如果是网络原因，恢复时间应该很快,下次重发的时候网络好了，进行正常的ack 在redis里面，不能设置消息的过期时间,可以用分布式定时任务，每隔一段时间
     * 去查redis里面有没有被消息确认的消息，然后取出来重新发送（存的时候，就要存如当前消息被发送的时间）
     */
    Map<String, Message> messageMap = new ConcurrentHashMap<String, Message>();
    /**
     * confirm机制,当生产者发送消息给exchange的时候,如果没有发到到exchange,会收不到ack,
     * 如果送达到了exchange,会回调该方法,如果消息，队列，交换机都设置了持久化，那么消息 在持久化到磁盘后，才会ack给生产者,也就是说生产者收到了ack后，消息肯定是可靠的了，已经
     * 到磁盘了
     */
    @PostConstruct
    public void init() {
        /**
         * 如果设置了spring.rabbitmq.publisher-confirms=true(默认是false),生产者会收到rabitmq-server返回的ack
         * 这个回调方法里面没有原始消息,相当于只是一个通知作用
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (null != messageMap && !messageMap.isEmpty()) {
                if (null != cause && !"".equals(cause)) {
                    System.out.println("失败原因:" + cause);
                    // 重发的时候到redis里面取,消费成功了，删除redis里面的msgId
                    Message message = messageMap.get(correlationData.getId());
                    rabbitTemplate.convertAndSend(sendExchange, rountKey, message, correlationData);
                } else {
                    messageMap.remove(correlationData.getId());
                    System.out.println("消息唯一标识:" + correlationData + ";确认结果:" + ack);
                }
            }
        });
        // rabbitTemplate.setMandatory(true);如果设置了mandatory=true(默认为false)
        // 这样设置的话，如果消息到达exchange后，没有queue与其绑定，会将消息返给生产者，生产者会
        // 回调这个方法
        rabbitTemplate
                .setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
                    System.out.println("send message failed: " + replyCode + " " + replyText);
                });
    }
    /**
     * 同步发送消息，效率低
     *
     * @param receiveMessage
     */
    public void syncSend(String receiveMessage) {
        Message message = MessageBuilder.withBody(receiveMessage.getBytes())
                .setContentType("application/json").build();
        // 同步等待的超时时间
        rabbitTemplate.setReplyTimeout(3 * 1000);
        Object receiveObject = rabbitTemplate.convertSendAndReceive(sendExchange, rountKey, message);
        System.out.println("生产者收到消费者返回的消息:" + receiveObject);
    }
    /**
     * 异步发送消息， 异步发送，性能更高，但是无法知道消息是否发送到了exchange,可以开启生产端的重试机制
     * spring.rabbitmq.template.retry.enabled=true，默认是false,另外 重试机制默认是重试3次，每次间隔一定时间再次重试,
     *
     * @param receiveMessage
     */
    public void asyncSend(String receiveMessage) {
        String msgId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(msgId);
        // 默认消息就是持久化的 MessageDeliveryMode deliveryMode = MessageDeliveryMode.PERSISTENT;
        Message message = MessageBuilder.withBody(receiveMessage.getBytes())
                .setContentType("application/json").setCorrelationIdString(msgId).build();
        messageMap.put(msgId, message);
        // 第4个参数是关联发布确定的参数
        try {
            // rabbitTemplate.setMandatory(true);
            // 如果不开启消息回调，可以不要第4个参数，因为在回调时，可以拿到这个correlationData
            // 最后会调用到 void basicPublish(String exchange, String routingKey, boolean mandatory,
            // BasicProperties props, byte[] body)
            // throws IOException;
            rabbitTemplate.convertAndSend(sendExchange, rountKey, message, correlationData);
            log.info("生产者发送消息:" + receiveMessage + ",消息Id：" + msgId);
        } catch (AmqpException e) {
            log.info("生产者发送消息:" + receiveMessage + "发生了异常:" + e.getMessage());
        }
    }


}
