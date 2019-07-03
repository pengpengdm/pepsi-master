package com.pepsi.stream;

import com.pepsi.util.WeightRandom;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-04 14:21
 * Description: No Description
 */
public class AggrFunctionDemo02 {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        DataStreamSource source = env.addSource(new SourceFunction<Tuple2<Integer, String>>() {
            private boolean isRunning = true;
            @Override
            public void run(SourceContext<Tuple2<Integer, String>> sourceContext) throws Exception {
                int count =0;
                while (isRunning) {
                    int randomNum = WeightRandom.randomNum();
                    System.out.println("current"+System.currentTimeMillis()/1000+"---"+count);
                    sourceContext.collect(new Tuple2<>(1, "pepsi"+randomNum));
                    //每0.1秒产生一条数据
                    Thread.sleep(100);
                    count++;
                }
            }
            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        source.keyBy(1).timeWindow(Time.seconds(10)).process(new ProcessWindowFunction<Tuple2<Integer, String>,Tuple2<Integer, String>, Tuple, TimeWindow>() {
            @Override
            public void process(Tuple tuple, Context context, Iterable<Tuple2<Integer, String>> iterable, Collector<Tuple2<Integer, String>> collector) throws Exception {
                for (Tuple2<Integer, String> ttt:iterable){
                    System.out.println(context.window().getStart());
                    System.out.println(context.window().getEnd());
                    System.out.println(ttt.f1);
                }

            }
        }).print();
        env.execute("pepsi cool");
    }

}
