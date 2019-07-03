package com.pepsi.checkpoint.fs;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-01 22:24
 * Description: No Description
 */
public class CheckPointMain {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStateBackend(new FsStateBackend("file:///Users/username/Documents/程序数据/Flink/checkpoint/"));
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        env.getCheckpointConfig().setCheckpointInterval(1000);

        DataStream<Tuple4<Integer,String,String,Integer>> data = env.setParallelism(4).addSource(new AutoSourceWithCp());
        env.setParallelism(4);
        data.keyBy(0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(2)))
                .apply(new WindowStatisticWithChk())
                .print();

        env.execute();
    }
}
