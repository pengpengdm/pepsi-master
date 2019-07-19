package com.pepsi.wordCountDemo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-17 14:20
 * Description:
 */
public class WordCountJobV3 {
    public static void main(String[] args) throws Exception {
        // 创建 execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // source
        DataStream<String> text = env.socketTextStream("localhost", 9001, "\n");

        // 解析数据，按 word 分组，开窗，聚合
        DataStream<Tuple2<String, Integer>> windowCounts = text
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                })
                .keyBy(0)
                //默认tumbling
                .timeWindow(Time.seconds(5))
                .sum(1);

        // sink
        windowCounts.print().setParallelism(1);
        //所有算子操作（例如创建源、聚合、打印）只是构建了内部算子操作的图形(StreamGraph)。只有在execute()被调用时才会在提交到集群上或本地计算机上执行。
        env.execute("Socket Window WordCount");
    }
}
