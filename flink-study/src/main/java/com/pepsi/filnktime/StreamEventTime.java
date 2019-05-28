package com.pepsi.filnktime;

import com.pepsi.filnktime.util.CustomDataSourceWithTimeStamp;
import com.pepsi.filnktime.util.TimeLagWatermarkGenerator;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-23 11:10
 * Description: 根据事件时间。每个事件在其生产设备上发生的时间,相当于延迟的处理。
 */
public class StreamEventTime {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //事件发生时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<Integer,Date>> source= env.addSource(new CustomDataSourceWithTimeStamp()).assignTimestampsAndWatermarks(new TimeLagWatermarkGenerator());

        source.keyBy(0).timeWindow(Time.seconds(5)).reduce(new ReduceFunction<Tuple2<Integer, Date>>() {
            @Override
            public Tuple2<Integer, Date> reduce(Tuple2<Integer, Date> integerDateTuple2, Tuple2<Integer, Date> t1) throws Exception {
                System.out.println("collect~~~");
                return new Tuple2<>(integerDateTuple2.f0+t1.f0,integerDateTuple2.f1);
            }
        }).print();
        env.execute("StreamEventTime");
    }

}
