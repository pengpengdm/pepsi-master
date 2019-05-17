package com.pepsi.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchemaWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-16 20:37
 * Description: 数据沉淀到 kafka 中
 */
public class StreamingKafkaSink {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        List<String> lists = new ArrayList<>();
        lists.add("pepsi start use flink01");
        lists.add("pepsi start use flink02");
        lists.add("pepsi start use flink03");
        DataStreamSource<String> source = env.fromCollection(lists);

        SingleOutputStreamOperator<String> outputStreamOperator = source.map(new MapFunction<String,String>() {
            @Override
            public String map(String ss) {
                return ss+",and i love it very much";
            }
        });
        Properties prop = new Properties();
        prop.setProperty("bootstrap.servers","118.24.100.168:9092");

        FlinkKafkaProducer011<String> myProducer = new FlinkKafkaProducer011<>("hellokafka", new KeyedSerializationSchemaWrapper<>(new SimpleStringSchema()), prop, FlinkKafkaProducer011.Semantic.EXACTLY_ONCE);
        outputStreamOperator.addSink(myProducer);
        env.execute("StreamingFromCollection");


    }
}
