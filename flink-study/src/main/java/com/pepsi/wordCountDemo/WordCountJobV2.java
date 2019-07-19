package com.pepsi.wordCountDemo;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-16 16:52
 * Description: 测试 AggregateFunction 的 merge 方法是否在 session window grouping aggregate 被调用.
 */
public class WordCountJobV2 {


    public static void main(String[] args) throws Exception {
        String hostname = "localhost";
        int port = 9001;

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> socketStram = env.socketTextStream(hostname,port);

        DataStream<Tuple2<String, Integer>> baseStram = socketStram.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] values = value.toLowerCase().split("\\W+");
                for (String str : values) {
                    out.collect(new Tuple2<String,Integer>(str,1));
                }
            }
        });
        baseStram.keyBy(0).timeWindow(Time.seconds(5)).process(new ProcessWindowFunction<Tuple2<String, Integer>, Tuple3<String, Integer,Long>, Tuple, TimeWindow>() {
            @Override
            public void process(Tuple tuple, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<Tuple3<String, Integer,Long>> out) throws Exception {
                TimeWindow  timeWindow = context.window();
                for(Tuple2<String, Integer> tuple2 : elements){
                    System.out.println(tuple2.f0);
                    out.collect(Tuple3.of(tuple2.f0,tuple2.f1,timeWindow.getEnd()));
                }
            }
        }).keyBy(2).timeWindow(Time.seconds(10)).aggregate(new AggregateFunction<Tuple3<String, Integer, Long>,Tuple2<String,Integer>,Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> createAccumulator() {
                return new Tuple2<>("",0);
            }

            @Override
            public Tuple2<String, Integer> add(Tuple3<String, Integer, Long> tuple3, Tuple2<String, Integer> acc) {
                if(StringUtils.isNullOrWhitespaceOnly(acc.f0)){
                    acc.setField(tuple3.f0,0);
                    acc.setField(tuple3.f1,0);
                }
                acc.setField(tuple3.f1+acc.f1,1);
                return acc;
            }

            @Override
            public Tuple2<String, Integer> getResult(Tuple2<String, Integer> acc) {
                System.out.println("get result");
                return acc;
            }

            @Override
            public Tuple2<String, Integer> merge(Tuple2<String, Integer> a, Tuple2<String, Integer> b) {
                b.setField("merge|"+b.f0,0);
                return b;
            }
        }).keyBy(0).timeWindow(Time.seconds(10)).aggregate(new AggregateFunction<Tuple2<String, Integer>, Integer, Integer>() {
            @Override
            public Integer createAccumulator() {
                return 0;
            }

            @Override
            public Integer add(Tuple2<String, Integer> tuple2, Integer accumulator) {
                return accumulator + tuple2.f1;
            }

            @Override
            public Integer getResult(Integer acc) {
                return acc;
            }

            @Override
            public Integer merge(Integer a, Integer b) {
                return b;
            }
        }).print();
        env.execute("WordCountJobV2");
    }

}
