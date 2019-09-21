package com.pepsi;

import com.pepsi.bean.NginxLogStats;
import com.pepsi.bean.RichNginxLog;
import com.pepsi.bean.codec.RichNginxLogCodec;
import com.pepsi.function.NginxLogAggregateFunction;
import com.pepsi.function.NginxLogProcessFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-19 12:03
 * Description: nginx 日志简单处理
 * 启动参数：
 *
 *
 */
public class SimpleNginxLogJob extends AbstractStreamJob {


    public static void main(String[] args) throws Exception {
        SimpleNginxLogJob job = new SimpleNginxLogJob();
        job.init(args);
        job.execute();

    }

    @Override
    protected void postInit() {
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    }

    @Override
    protected void preExecute() {
        RichNginxLogCodec codec = new RichNginxLogCodec();
        FlinkKafkaConsumerBase<RichNginxLog> kafkaSource = createKafkaSource(codec);
        DataStream<RichNginxLog> stream = env.addSource(kafkaSource);
        NginxLogProcessFunction processFunction = new NginxLogProcessFunction();
        NginxLogAggregateFunction aggregateFunction = new NginxLogAggregateFunction();
        BoundedOutOfOrdernessTimestampExtractor<RichNginxLog> timeAssign = new BoundedOutOfOrdernessTimestampExtractor<RichNginxLog>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(RichNginxLog element) {
                return element.getTimestamp();
            }
        };
        DataStream<NginxLogStats> statsStream = stream.assignTimestampsAndWatermarks(timeAssign).process(processFunction)
                .keyBy((KeySelector<RichNginxLog, String>) RichNginxLog::getPath)
                .timeWindow(Time.seconds(30))
                .aggregate(aggregateFunction);
        statsStream.print();
    }
}
