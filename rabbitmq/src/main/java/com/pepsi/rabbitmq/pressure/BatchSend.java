package com.pepsi.rabbitmq.pressure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pepsi.rabbitmq.conf.MessageVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/25
 * describe:
 */
@Component
public class BatchSend {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Scheduled(cron = "* 0/1 * * * ?")
    public void batch() throws JsonProcessingException {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        PressureMsgDto pressureMsgDto = new PressureMsgDto("333","123");
        MessageVO messageVO = new MessageVO();
        messageVO.setMessageContent(pressureMsgDto);
        String json = jsonObjectMapper.writeValueAsString(pressureMsgDto);
        System.out.println(json);
        rabbitTemplate.convertAndSend("direct.exchange.onstar.pepsi","direct.exchange",messageVO.getMessageContent());
        /*for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("123123123>>>>>>");
                    //rabbitTemplate.convertAndSend("onstar.service.topic.orderservice","direct.exchange","");
                }
            });
        }*/
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper jsonObjectMapper = new ObjectMapper();
        PressureMsgDto pressureMsgDto = new PressureMsgDto("333","123");
        String s =  jsonObjectMapper.writeValueAsString(pressureMsgDto);
        System.out.println(">>>>>"+s);
        String s2 = jsonObjectMapper.writeValueAsString(s);
        System.out.println("<<<<<<<"+s2);
        String s3 = jsonObjectMapper.writeValueAsString(s2);
        System.out.println("-------"+s3);
        String jsons = "{\"name\":\"333\",\"vin\":\"123\"}";

    }


}
