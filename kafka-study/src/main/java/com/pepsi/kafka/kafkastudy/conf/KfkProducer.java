package com.pepsi.kafka.kafkastudy.conf;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-01 15:51
 * Description: No Description
 */
@Component
public class KfkProducer {

    @Bean
    public Producer producer(){
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","118.24.100.168:9092");
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = null;
        try{
            producer = new KafkaProducer<>(properties) ;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return producer;
    }




}
