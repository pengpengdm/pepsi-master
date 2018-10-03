package com.pepsi.rabbitmq.conf;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/26
 * describe:
 */
public class MessageVO {
    private String exchangeName;
    private String routeKey;
    private Object messageContent;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Object getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(Object messageContent) {
        this.messageContent = messageContent;
    }
}
