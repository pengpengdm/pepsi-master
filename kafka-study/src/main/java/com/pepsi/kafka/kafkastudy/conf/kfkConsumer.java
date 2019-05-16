package com.pepsi.kafka.kafkastudy.conf;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-16 11:47
 * Description: No Description
 */
@Component
public class kfkConsumer {

    @Bean
    public Consumer consumer(){

        Properties props = new Properties();
        props.put("bootstrap.servers","118.24.100.168:9092");
        props.put("enable.auto.commit", "true");
        props.put("group.id", "groupId");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("max.poll.records", 1000);
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("hellokafka"));
        return consumer;

    }


}
