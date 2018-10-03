//package com.pepsi.rabbitmq.conf;
//
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConversionException;
//
///**
// * @author pepsi
// * @version 1.0
// * @date 2018/09/26
// * describe:
// */
//
//public class PepsiMessageConverter extends Jackson2JsonMessageConverter {
//
//    @Override
//    public Object fromMessage(Message message) throws MessageConversionException {
//        message.getBody();
//        return super.fromMessage(message);
//    }
//
//    @Override
//    public Object fromMessage(Message message, Object conversionHint) throws MessageConversionException {
//        return super.fromMessage(message, conversionHint);
//    }
//
//}
