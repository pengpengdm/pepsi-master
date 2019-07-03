package com.pepsi.stream.custom;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-03 22:57
 * Description: No Description
 */
public class CustomSource02 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<NgLogSpan> source = env.addSource(new SourceFunction<NgLogSpan>() {
            private boolean isRunning = true;
            private long count = 1L;

            @Override
            public void run(SourceFunction.SourceContext<NgLogSpan> sourceContext) throws Exception {
                while (isRunning) {
                    sourceContext.collect(new NgLogSpan(String.valueOf(count % 10),  System.currentTimeMillis()));
                    count++;
                    //每秒产生一条数据
                    Thread.sleep(1000);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        // env
        // 创建数据源，得到 UserBehavior 类型的 DataStream
        //.createInput(csvInput, pojoType)
        source.keyBy("word","count")
                .timeWindow(Time.seconds(10))
                .aggregate(new AggregateFunction<NgLogSpan, NgLogSpan, NgLogSpan>() {
                    @Override
                    public NgLogSpan createAccumulator() {
                        return new NgLogSpan("pepsi",1L);
                    }

                    @Override
                    public NgLogSpan add(NgLogSpan ngLogSpan, NgLogSpan ngLogSpan2) {
                        return ngLogSpan;
                    }

                    @Override
                    public NgLogSpan getResult(NgLogSpan ngLogSpan) {
                        return ngLogSpan;
                    }

                    @Override
                    public NgLogSpan merge(NgLogSpan ngLogSpan, NgLogSpan acc1) {
                        return ngLogSpan;
                    }
                }, new ProcessWindowFunction<NgLogSpan, NgLogSpan, Tuple, TimeWindow>() {
                    @Override
                    public void process(Tuple s, Context context, Iterable<NgLogSpan> iterable, Collector<NgLogSpan> collector) throws Exception {
                        for(NgLogSpan ss:iterable){
                            collector.collect(ss);
                        }
                    }
                }).print();
        env.execute("ssssss");
    }

    //窗口
    public static class NgLogSpan{
        public String word;
        public long count;
        public  NgLogSpan(){}
        public NgLogSpan(String value, long l) {
            this.count = l;
            this.word= value;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "NgLogSpan{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
