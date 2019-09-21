package com.pepsi;

import com.pepsi.function.AllowLatenessAggregationFunction;
import com.pepsi.function.AllowLatenessFlapMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-21 13:30
 * Description: 测试  allowedLateness 方法
 */
public class AllowedLatenessTestJob extends AbstractStreamJob{


    public static void main(String[] args) throws Exception {
        AllowedLatenessTestJob job = new AllowedLatenessTestJob();
        job.init(args);
        job.execute();
    }

    @Override
    protected void postInit() {
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }

    @Override
    protected void preExecute() {
        AllowLatenessFlapMapFunction mapFunction = new AllowLatenessFlapMapFunction();
        AllowLatenessAggregationFunction aggrFunction = new AllowLatenessAggregationFunction();
        BoundedOutOfOrdernessTimestampExtractor<Tuple2<String,Long>> timeAssign = new BoundedOutOfOrdernessTimestampExtractor<Tuple2<String,Long>>(Time.seconds(1)) {
            @Override
            public long extractTimestamp(Tuple2<String,Long> element) {
                return element.f1;
            }
        };
        DataStream<Tuple3<String,String,Integer>> stream = env.socketTextStream("127.0.0.1",9001,"\n")
                .flatMap(mapFunction)
                .assignTimestampsAndWatermarks(timeAssign)
                .keyBy((KeySelector<Tuple2<String, Long>, String>) value -> value.f0)
                .timeWindow(Time.seconds(10))
//                .allowedLateness(Time.minutes(5))
                .aggregate(aggrFunction);
        stream.print();
    }
}
