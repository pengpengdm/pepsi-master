package com.pepsi;

import com.pepsi.function.AllowLatenessAggregationFunction;
import com.pepsi.function.AllowLatenessFlapMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-21 13:30
 * Description: 测试 maxOutOfOrderness 方法（allowedLateness 注释)
 * 3秒的窗口，10s的延迟。swatermark 覆盖到时间所在的窗口结束时间即可出发结算（1、窗口内有数据。2、watermark > 窗口结束时间)。
 * 第一次数据 2019-06-01 14:36:10 (1559370970000) 会落在 [09-12)这个窗口内。那么这个窗口的计算时间必然是 watermark 要到 2019-06-01 14:36:22
 * 测试：
 * 第二次数据： 2019-06-01 14:36:15
 * 第三次数据： 2019-06-01 14:36:20
 * 第四次数据： 2019-06-01 14:36:22
 *
 *
 * 第二：测试 allowedLateness 在allowedLateness时间内，每条数据都会出发一次window结算。业务需要实现幂等。
 *
 *
 *
 */
public class AllowedLatenessTestJob extends AbstractStreamJob {


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
//
        BoundedOutOfOrdernessTimestampExtractor<Tuple2<String, Long>> timestampAssigner = new BoundedOutOfOrdernessTimestampExtractor<Tuple2<String, Long>>(Time.seconds(10)) {
            private static final long serialVersionUID = 6762201324392750913L;
            @Override
            public long extractTimestamp(Tuple2<String, Long> element) {
                return element.f1;
            }
        };

        env.socketTextStream("127.0.0.1", 9001, "\n")
                .flatMap(mapFunction)
                .assignTimestampsAndWatermarks(timestampAssigner)
                .keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.seconds(3)))
                .allowedLateness(Time.minutes(5))
                .aggregate(aggrFunction)
                .print();
    }
}
