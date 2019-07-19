package com.pepsi.nglog;

import com.pepsi.nglog.codec.NginxLogJsonCodec;
import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.nglog.function.NginxLogSummaryAggregationFunctionForTuple3;
import com.pepsi.nglog.function.NginxLogSummaryWindowFunction;
import com.pepsi.nglog.function.TopKWindowSummary;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-09 11:33
 * Description: topK结果统计
 */
public class TopKSummaryJob extends AbstractStreamingJob{


    public static void main(String[] args) throws Exception {
        TopKSummaryJob topKSummaryJob = new TopKSummaryJob();
        topKSummaryJob.init(args);
        topKSummaryJob.execute();
    }

    @Override
    protected void preExecute() {
        // Kafka source
        NginxLogJsonCodec codec = new NginxLogJsonCodec();
        FlinkKafkaConsumerBase<RichNginxLog> kafkaSource = createKafkaSource(codec);

        DataStreamSource<RichNginxLog> baseStream = env.addSource(kafkaSource);
        SingleOutputStreamOperator<TopKWindowSummary<RichNginxLog>> summaryStreamByPath = baseStream.map(new RichMapFunction<RichNginxLog, Tuple3<Integer,String,RichNginxLog>>() {
            @Override
            public Tuple3<Integer, String, RichNginxLog> map(RichNginxLog value) throws Exception {
                return Tuple3.of(getRuntimeContext().getIndexOfThisSubtask(),value.getDomain()+"|"+value.getPath(),value);
            }
        }).keyBy(a->a.f0)
                .window(TumblingEventTimeWindows.of(Time.minutes(5)))
                .aggregate(new NginxLogSummaryAggregationFunctionForTuple3("",5),new NginxLogSummaryWindowFunction()).windowAll(TumblingEventTimeWindows.of(Time.minutes(5)))
                .reduce((value1, value2) -> {
                    value1.merge(value2);
                    return value1;
                });
        summaryStreamByPath.print();
    }
}
