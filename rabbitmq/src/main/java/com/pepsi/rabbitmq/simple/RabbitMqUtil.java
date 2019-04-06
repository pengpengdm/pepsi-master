package com.pepsi.rabbitmq.simple;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/11/28
 * describe:
 */
public class RabbitMqUtil {
    private RabbitMqUtil() {}
    /**
     * 获取消息被重试的次数
     *
     * @param messageProperties
     * @return
     */
    @SuppressWarnings("unchecked")
    public static long getRetryCount(MessageProperties messageProperties) {
        Long retryCount = 0L;
        if (null != messageProperties) {
            Map<String, Object> headers = messageProperties.getHeaders();
            if (null != headers && !headers.isEmpty()) {
                if (headers.containsKey("x-death")) {
                    List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
                    if (deaths.size() > 0) {
                        Map<String, Object> death = deaths.get(0);
                        retryCount = (Long) death.get("count");
                    }
                }
            }
        }
        return retryCount;
    }
    /**
     * 获取原始的routingKey,这个key主要用来追踪一开始的routing-key
     *
     * @param messageProperties AMQP消息属性
     * @param defaultValue 默认值
     * @return 原始的routing-key
     */
    private static String getOrigRoutingKey(MessageProperties messageProperties,
                                            String defaultValue) {
        String routingKey = defaultValue;
        if (null != messageProperties) {
            Map<String, Object> headers = messageProperties.getHeaders();
            if (null != headers && !headers.isEmpty()) {
                if (headers.containsKey("x-orig-routing-key")) {
                    routingKey = headers.get("x-orig-routing-key").toString();
                }
            }
        }
        return routingKey;
    }
    private static MessageProperties createOverrideProperties(MessageProperties messageProperties,
                                                              Map<String, Object> headers) {
        MessageProperties newMsgProperties = new MessageProperties();
        newMsgProperties.setContentType(messageProperties.getContentType());
        newMsgProperties.setContentEncoding(messageProperties.getContentEncoding());
        // 从已有的properties中创建新的properties，使用提供的headers字段覆盖已有的headers
        for (final Map.Entry<String, Object> mapHeaders : headers.entrySet()) {
            newMsgProperties.setHeader(mapHeaders.getKey(), mapHeaders.getValue());
        }
        newMsgProperties.setDeliveryMode(messageProperties.getDeliveryMode());
        newMsgProperties.setPriority(messageProperties.getPriority());
        newMsgProperties.setCorrelationId(messageProperties.getCorrelationId());
        newMsgProperties.setReplyTo(messageProperties.getReplyTo());
        newMsgProperties.setExpiration(messageProperties.getExpiration());
        newMsgProperties.setMessageId(messageProperties.getMessageId());
        newMsgProperties.setTimestamp(messageProperties.getTimestamp());
        newMsgProperties.setType(messageProperties.getType());
        newMsgProperties.setClusterId(messageProperties.getClusterId());
        newMsgProperties.setUserId(messageProperties.getUserId());
        newMsgProperties.setAppId(messageProperties.getAppId());
        newMsgProperties.setConsumerQueue(messageProperties.getConsumerQueue());
        newMsgProperties.setConsumerTag(messageProperties.getConsumerTag());
        return newMsgProperties;
    }
    public static Message buildMessage(Message message) {
        Map<String, Object> headers = new HashMap<>();
        return buildeLastMessage(message, headers);
    }
    public static Message buildMessage2(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if (null == headers || headers.isEmpty()) {
            headers = new ConcurrentHashMap<String, Object>();
        }
        return buildeLastMessage(message, headers);
    }
    private static Message buildeLastMessage(Message message, Map<String, Object> headers) {
        headers.put("x-orig-routing-key", getOrigRoutingKey(message.getMessageProperties(),
                message.getMessageProperties().getReceivedRoutingKey()));
        MessageProperties messageProperties =
                createOverrideProperties(message.getMessageProperties(), headers);
        Message newMessage = new Message(message.getBody(), messageProperties);
        return newMessage;
    }

}
