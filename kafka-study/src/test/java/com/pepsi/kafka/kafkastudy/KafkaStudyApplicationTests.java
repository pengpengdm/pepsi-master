package com.pepsi.kafka.kafkastudy;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaStudyApplicationTests {

    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    @Test
    public void procuder() {
        String msg = "hello,kafka!";
        try  {
            ProducerRecord<String, String> record = new ProducerRecord<>("hellokafka",msg);
            producer.send(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void consumer(){
        int messageNo = 1;
        try {
            for (;;) {
                ConsumerRecords<String,String> msgList = consumer.poll(Duration.ofSeconds(5));
                if(null!=msgList&&msgList.count()>0){
                    for (ConsumerRecord<String, String> record : msgList) {
                        System.out.println(messageNo+"=======receive: key = " + record.key() + ", value = " + record.value()+" offset==="+record.offset());
                    }
                }else{
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }


    @Test
    public void sender(){
        int sendCount=0;
        for (;;){
            sendCount++;
            String msg = "hello,myflink，second send,this is "+sendCount+" data";
            ProducerRecord<String, String> record = new ProducerRecord<>("hellokafka",msg);
            producer.send(record);
            if(sendCount%10==0){
                System.out.println("send sendCount="+sendCount);
            }
            if(sendCount>100){
                break;
            }

        }

    }
}
