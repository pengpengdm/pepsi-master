package com.pepsi.rabbitmq.pressure;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/25
 * describe:
 */
@Component
public class MsgReceiver {

    /*@RabbitListener(bindings = @QueueBinding(value = @Queue("direct.quque.onstar.pepsi"),exchange=@Exchange("direct.exchange.onstar.pepsi"),key = "direct.exchange"))
    public void procee(@Payload PressureMsgDto inputDto, @Headers Map<String, Object> map) throws IOException {
//        String string = new String(bytes);
//        System.out.println(string);
//        JSONObject jsonObject = JSONObject.parseObject(string);
//        System.out.println(string);
//        ObjectMapper jsonObjectMapper = new ObjectMapper();
//        PressureMsgDto pressureMsgDto = jsonObjectMapper.readValue(string,PressureMsgDto.class);
        System.out.println("~~~~~~~~~~"+inputDto.toString());

    }*/


    public static void main(String[] args) throws IOException {
        String jsonstr ="{\"vin\":\"123\",\"name\":\"pepsi\"}";
        System.out.println(jsonstr.length());
        byte[] jsonBytes = jsonstr.getBytes();
        byte[] bytes = new byte[]{34, 123, 92, 34, 118, 105, 110, 92, 34, 58, 92, 34, 49, 50, 51, 92, 34, 44, 92, 34, 110, 97, 109, 101, 92, 34, 58, 92, 34, 112, 101, 112, 115, 105, 92, 34, 125, 34};
        String str = new String(bytes,"UTF-8");
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        PressureMsgDto pressureMsgDto = jsonObjectMapper.readValue(str,PressureMsgDto.class);
        System.out.println("~~~~~~~~~~"+pressureMsgDto.toString());
    }
}
