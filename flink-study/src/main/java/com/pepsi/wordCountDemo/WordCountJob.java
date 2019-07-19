package com.pepsi.wordCountDemo;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-15 10:58
 * Description: flink hello world。
 */
public class WordCountJob {


    public static void main(String[] args) throws Exception {
        if (args.length != 2){
            System.err.println("USAGE:\nSocketTextStreamWordCount <hostname> <port>");
            return;
        }

        String hostName = args[0];
        Integer port = Integer.parseInt(args[1]);

        StreamExecutionEnvironment env  = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> baseSource = env.socketTextStream(hostName,port);
//        StreamExecutionEnvironment env  = StreamExecutionEnvironment.createLocalEnvironment();
//        DataStreamSource<String> baseSource = env.socketTextStream("localhost",9001);

        DataStream<Tuple2<String, Integer>> stream = baseSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] values = value.toLowerCase().split("\\W+");
                for (String str : values) {
                    collector.collect(new Tuple2<String,Integer>(str,1));
                }
            }
        }).keyBy(0)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(5),Time.seconds(3)))
                .aggregate(new AggregateFunction<Tuple2<String, Integer>, Tuple2<String,Integer>, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> createAccumulator() {
                        return new Tuple2<String, Integer>("",0);
                    }
                    @Override
                    public Tuple2<String, Integer> add(Tuple2<String, Integer> value, Tuple2<String, Integer> acc) {
                        if(StringUtils.isNullOrWhitespaceOnly(acc.f0)){
                            acc.setField(value.f0,0);
                        }
                        acc.setField(value.f1+acc.f1,1);
                        return acc;
                    }

                    @Override
                    public Tuple2<String, Integer> getResult(Tuple2<String, Integer> accumulator) {
                        System.out.println("get result");
                        return accumulator;
                    }

                    @Override
                    public Tuple2<String, Integer> merge(Tuple2<String, Integer> a, Tuple2<String, Integer> b) {
                        return b;
                    }
                }).windowAll(SlidingProcessingTimeWindows.of(Time.seconds(5),Time.seconds(3))).reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                        value2.setField(value2.f0+"|"+value1.f0,0);
                        value2.setField(value2.f1+value1.f1,1);
                        return value2;
                    }
                });
        stream.print();
        env.execute("wordcount job");
    }

}
