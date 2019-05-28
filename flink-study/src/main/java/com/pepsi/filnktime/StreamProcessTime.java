package com.pepsi.filnktime;

import com.pepsi.filnktime.util.CustomDataSource;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-23 11:10
 * Description: 处理时间，当前机器处理该条事件的时间,每分支执行一次。
 *              是指：如果应用程序在上午9:15:34开始运行，则第一个分钟处理时间窗口将包括在上午9:15:34到上午9:16:00之间处理的事件，
 *              下一个窗口将包括在上午9:16:00到9:17:00之间处理的事件
 */
public class StreamProcessTime {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        DataStreamSource<Tuple3<Integer,Integer, Date>> source  = env.addSource(new CustomDataSource());

        source.keyBy(1).timeWindow(Time.seconds(10)).reduce(new ReduceFunction<Tuple3<Integer, Integer, Date>>() {
            @Override
            public Tuple3<Integer, Integer, Date> reduce(Tuple3<Integer, Integer, Date> value01, Tuple3<Integer, Integer, Date> value02) throws Exception {
                System.out.println(value01.f0+"------"+value02.f0);
                return new Tuple3<>(value01.f0, 111+value02.f1, value01.f2);
            }
        }).print();
        env.execute("StreamProcessTime");
    }




}
