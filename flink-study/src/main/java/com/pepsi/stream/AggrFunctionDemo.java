package com.pepsi.stream;

import com.pepsi.util.WeightRandom;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-03 22:55
 * Description: No Description
 */
public class AggrFunctionDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<Tuple3<Integer, String, Long>> source = env.addSource(new SourceFunction<Tuple3<Integer, String,Long>>() {
            private boolean isRunning = true;
            private long count = 1L;
            @Override
            public void run(SourceContext<Tuple3<Integer, String,Long>> sourceContext) throws Exception {
                while (isRunning) {

                    sourceContext.collect(new Tuple3<>(1, "pepsi"+WeightRandom.randomNum(),System.currentTimeMillis()));
                    count++;
                    //每0.1秒产生一条数据
                    Thread.sleep(1000);
                }
            }
            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        source.flatMap(new RichFlatMapFunction<Tuple3<Integer, String, Long>, Tuple3<Integer, String, Long>>() {
            @Override
            public void flatMap(Tuple3<Integer, String, Long> tuple3, Collector<Tuple3<Integer, String, Long>> collector) throws Exception {

            }
        }).print();


        /*source.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple3<Integer, String, Long>>() {
            @Override
            public long extractTimestamp(Tuple3<Integer, String, Long> tuple3, long l) {
                return tuple3.f2;
            }
            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                //水位 延迟一秒
                return new Watermark(System.currentTimeMillis() - 10*1000L);
            }
        }).keyBy(1).window(TumblingEventTimeWindows.of(Time.seconds(10))).sum(0).print();*/

        source.keyBy(0).window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .aggregate(new AggregateFunction<Tuple3<Integer, String,Long>, Tuple3<Integer, String,Long>, Tuple3<Integer, String,Long>>() {
                    @Override
                    public Tuple3<Integer, String,Long> createAccumulator() {
                        Tuple3<Integer, String,Long> tt = new Tuple3<>();
                        tt.setFields(0,"papapa",System.currentTimeMillis());
                        return tt;
                    }

                    @Override
                    public Tuple3<Integer, String,Long> add(Tuple3<Integer, String,Long> value, Tuple3<Integer, String,Long> accumulator) {
                        if (accumulator.f0 == null) {
                            accumulator.setFields(0, "ppp",System.currentTimeMillis());
                        }
                        accumulator.setFields(value.f0 + accumulator.f0, value.f1,accumulator.f2);
                        return accumulator;
                    }

                    @Override
                    public Tuple3<Integer, String,Long> getResult(Tuple3<Integer, String,Long> longStringTuple2) {
                        return longStringTuple2;
                    }

                    @Override
                    public Tuple3<Integer, String,Long> merge(Tuple3<Integer, String,Long> a, Tuple3<Integer, String,Long> b) {
                        a.setFields(a.f0 + b.f0, a.f1,a.f2);
                        return a;
                    }
                }, new ProcessWindowFunction<Tuple3<Integer, String,Long>, Tuple3<Integer, String,Long>, Tuple, TimeWindow>() {
                    @Override
                    public void process(Tuple tuple, Context context, Iterable<Tuple3<Integer, String,Long>> iterable, Collector<Tuple3<Integer, String,Long>> collector) throws Exception {
                        for (Tuple3<Integer, String,Long> it : iterable) {
                            // 使用窗口时间作为时间戳, 保持一致
                            collector.collect(it);
                        }
                    }
                })./*windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10))).allowedLateness(Time.seconds(5)).process(
                        new ProcessAllWindowFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, TimeWindow>() {
                            private int topSize = 10;

                            @Override
                            public void process(Context context, Iterable<Tuple2<Integer, String>> iterable, Collector<Tuple2<Integer, String>> collector) throws Exception {
                                TreeMap<Integer, Tuple2<Integer, String>> treemap = new TreeMap<Integer, Tuple2<Integer, String>>(new Comparator<Integer>() {
                                    @Override
                                    public int compare(Integer o1, Integer o2) {
                                        return o2 - o1;
                                    }
                                });

                                for (Tuple2<Integer, String,Long> element : iterable) {
                                    treemap.put(element.f0, element);
                                    if (treemap.size() > topSize) {
                                        treemap.pollLastEntry();
                                    }
                                }
                                for (Map.Entry<Integer, Tuple2<Integer, String>> entry : treemap
                                        .entrySet()) {
                                    collector.collect(entry.getValue());
                                }
                            }
                     }).*/print();
        env.execute("AggrFunctionDemo");
    }
}
