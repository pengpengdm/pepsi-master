package com.pepsi.stream;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.operators.StreamSource;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-18 11:36
 * Description: 全量的聚合。
 */
public class StreamFullAggr {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source= env.socketTextStream("localhost",9000);

        DataStream<Tuple2<Integer, Integer>> initDate = source.map(new MapFunction<String, Tuple2<Integer,Integer>>() {
            @Override
            public Tuple2<Integer,Integer> map(String s) throws Exception {
                return new Tuple2<>(1, Integer.valueOf(s));
            }
        });
        initDate.keyBy(0).timeWindow(Time.seconds(5)).process(new ProcessWindowFunction<Tuple2<Integer, Integer>, Object, Tuple, TimeWindow>() {
            @Override
            public void process(Tuple key, Context context, Iterable<Tuple2<Integer, Integer>> elements, Collector<Object> collector) throws Exception {
                System.out.println("executor process....");
                long count = 0;
                for(Tuple2<Integer,Integer> element: elements){
                    count++;
                }
                collector.collect("window:"+context.window()+",count:"+count);
            }
        }).print();
        env.execute("StreamFullAggr");
    }

}
