//package com.pepsi.rabbitmq.conf;
//
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.AmqpIllegalStateException;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageListener;
//import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
//
//import java.lang.reflect.Method;
//
///**
// * @author pepsi
// * @version 1.0
// * @date 2018/09/26
// * describe:
// */
//public class MessageListenerAdapter4Jackson extends MessageListenerAdapter {
//
//    @Override
//    public void onMessage(Message message,Channel channel) throws Exception {
//        // Check whether the delegate is a MessageListener impl itself.
//        // In that case, the adapter will simply act as a pass-through.
//        Object delegate = getDelegate();
//        if (delegate != this) {
//            if (delegate instanceof ChannelAwareMessageListener) {
//                if (channel != null) {
//                    ((ChannelAwareMessageListener) delegate).onMessage(message, channel);
//                    return;
//                } else if (!(delegate instanceof MessageListener)) {
//                    throw new AmqpIllegalStateException("MessageListenerAdapter cannot handle a"
//                            +"ChannelAwareMessageListener delegate if it hasn't been invoked with a Channel itself");
//                }
//            }
//            if (delegate instanceof MessageListener) {
//                ((MessageListener) delegate).onMessage(message);
//                return;
//            }
//        }
//        String methodName = getListenerMethodName(message, null);
//        if (methodName == null) {
//            throw new AmqpIllegalStateException("No default listener method specified:"
//                    +"Either specify a non-null value for the 'defaultListenerMethod' property or"
//                    +"override the 'getListenerMethodName' method.");
//        }
//        Method[] methods = delegate.getClass().getMethods();
//        for (Method method : methods) {
//            if (method.getName().equals(methodName) && method.getParameterTypes().length == 1) {
//                String className = method.getParameterTypes()[0].getName();
//                message.getMessageProperties().getHeaders().put(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME, className);
//                Object convertedMessage = extractMessage(message);
//                Object result = method.invoke(delegate, convertedMessage);
//                if (result != null) {
//                    handleResult(result, message, channel);
//                }
//                return;
//            }
//        }
//    }
//}
