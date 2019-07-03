package com.pepsi.stream;

import com.pepsi.util.WeightRandom;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.runtime.query.TaskKvStateRegistry;
import org.apache.flink.runtime.state.*;
import org.apache.flink.runtime.state.filesystem.AbstractFileStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.ttl.TtlTimeProvider;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-04 17:42
 * Description: No Description
 */
public class StateDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        OutputTag<Tuple2<Integer, String>> outputTag = new OutputTag<>("direct-output");

        DataStreamSource<Tuple2<Integer, String>> source = environment.addSource(new SourceFunction<Tuple2<Integer, String>>() {
            private boolean isRunning = true;
            @Override
            public void run(SourceContext<Tuple2<Integer, String>> sourceContext) throws Exception {
                while (isRunning) {
                    int randomNum = WeightRandom.randomNum();
                    sourceContext.collect(new Tuple2<>(1, "pepsi"+randomNum));
                    //每0.1秒产生一条数据
                    Thread.sleep(1000);
                }
            }
            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        source.process(new ProcessFunction<Tuple2<Integer, String>,Tuple2<Integer, String>>() {
            private  Map map;
            @Override
            public void processElement(Tuple2<Integer, String> tuple2, Context context, Collector<Tuple2<Integer, String>> collector) throws Exception {

            }

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                map = new HashMap<>();
            }

        }).timeWindowAll(Time.seconds(10));



        environment.execute("pepsi is cool");
    }
}
