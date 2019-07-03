package com.pepsi.stream;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-16 11:33
 * Description: kafka作为源
 */
public class StreamKafkaSource {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //checkpoint配置
        env.enableCheckpointing(5000);
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        String topic = "hellokafka";
        Properties props = new Properties();

        props.put("bootstrap.servers","118.24.100.168:9092");
        props.put("group.id", "groupId");
        props.put("enable.auto.commit", "true");

        FlinkKafkaConsumer010<String> consumer = new FlinkKafkaConsumer010<>(topic, new SimpleStringSchema(), props);
        consumer.setStartFromGroupOffsets();//默认消费策略

        DataStreamSource<String> text = env.addSource(consumer);

        text.print().setParallelism(1);

        env.execute("StreamKafkaSource");

    }
}
