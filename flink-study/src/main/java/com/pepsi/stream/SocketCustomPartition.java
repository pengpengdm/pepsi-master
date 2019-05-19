package com.pepsi.stream;

import com.pepsi.stream.custom.PepsiCustomPartition;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/19
 * describe: sockey 数据源 使用自定义分区，
 * 通过分区器来精确得控制数据在算子之间的流向。
 */
public class SocketCustomPartition {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> sourceText =  env.socketTextStream("localhost",9000);

        DataStream<Tuple1<String>> initData = sourceText.map(new MapFunction<String, Tuple1<String>>() {
            @Override
            public Tuple1<String> map(String s) throws Exception {
                return new Tuple1<>(s);
            }
        });
        //分区之后的数据
        DataStream<Tuple1<String>> partitionData = initData.partitionCustom(new PepsiCustomPartition(),0);

        partitionData.map(new MapFunction<Tuple1<String>, Object>() {
            @Override
            public Object map(Tuple1<String> tuple1) throws Exception {
                System.out.println("当前线程id：" + Thread.currentThread().getId() + ",value: " + tuple1);
                return tuple1;
            }
        }).print().setParallelism(1);

        env.execute("SocketCustomPartition");
    }

}
