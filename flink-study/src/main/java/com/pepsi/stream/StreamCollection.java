package com.pepsi.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-16 11:26
 * Description: 集合作为源
 */
public class StreamCollection {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        DataStreamSource<Integer> streamSource = env.fromCollection(list);

        SingleOutputStreamOperator<Integer> num = streamSource.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer integer) throws Exception {
                return integer+1;
            }
        });
        num.print();
        env.execute("StreamCollection");
    }
}
