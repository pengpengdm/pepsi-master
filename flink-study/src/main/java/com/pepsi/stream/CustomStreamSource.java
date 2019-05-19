package com.pepsi.stream;

import com.pepsi.stream.custom.PepsiCustomStreamSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/19
 * describe: 自定义 StreamSource
 */
public class CustomStreamSource {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Long> source = env.addSource(new PepsiCustomStreamSource());

        DataStream<Tuple1<Long>> initData = source.map(new MapFunction<Long, Tuple1<Long>>() {
            @Override
            public Tuple1<Long> map(Long aLong) throws Exception {
                System.out.println(aLong);
                return new Tuple1<>(aLong);
            }
        });
        initData.print();
        env.execute("CustomStreamSource");
    }
}
