package com.pepsi.nglog;

import com.pepsi.nglog.codec.NginxLogJsonCodec;
import com.pepsi.nglog.dto.NginxLogStats;
import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.nglog.function.NginxLogEsSink;
import com.pepsi.nglog.function.NginxLogStatsEsSink;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-08 11:08
 * Description: 测试多个shuffer下 task 和 slot 情况。
 */

public class NginxLogSummaryJobV2 extends AbstractStreamingJob{



    public static void main(String[] args) throws Exception {
        NginxLogSummaryJobV2 shufferJob = new NginxLogSummaryJobV2();
        shufferJob.init(args);
        shufferJob.execute();
    }

    @Override
    protected void preExecute() {
        // Kafka source
        NginxLogJsonCodec codec = new NginxLogJsonCodec();
        FlinkKafkaConsumerBase<RichNginxLog> kafkaSource = createKafkaSource(codec);
        DataStreamSource<RichNginxLog> baseStream = env.addSource(kafkaSource);
        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
        String esServers = cfg.getString("es_nginx_servers", null);

        NginxLogEsSink esSink = new NginxLogEsSink(esServers, null, true, 1);

        KeyedStream<Tuple2<String,RichNginxLog>,String> keyedStream = baseStream
                .map(new MapFunction<RichNginxLog, Tuple2<String, RichNginxLog>>() {
                    @Override
                    public Tuple2<String, RichNginxLog> map(RichNginxLog value) throws Exception {
                        return Tuple2.of(value.getDomain() + "|" + value.getPath(), value);
                    }
                }).process(new ProcessFunction<Tuple2<String, RichNginxLog>, Tuple2<String, RichNginxLog>>() {

                    @Override
                    public void processElement(Tuple2<String, RichNginxLog> value, Context ctx, Collector<Tuple2<String, RichNginxLog>> out) throws Exception {
                        out.collect(value);
                    }

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                    }
                }).keyBy(a -> a.f0);

        SingleOutputStreamOperator<NginxLogStats> aggSgtream = keyedStream.timeWindow(Time.seconds(10)).aggregate(new AggregateFunction<Tuple2<String, RichNginxLog>, NginxLogStats, NginxLogStats>() {
            @Override
            public NginxLogStats createAccumulator() {
                return new NginxLogStats();
            }

            @Override
            public NginxLogStats add(Tuple2<String, RichNginxLog> input, NginxLogStats acc) {
                String key = input.f0;
                RichNginxLog value = input.f1;
                if (acc.getKey() == null) {
                    acc.setKey(key);
                    acc.setDomain(value.getDomain());
                    acc.setUrlPath(value.getPath());
                    acc.setUpstreamAddr(value.getUpstream());
                    acc.setStatus(value.getStatus());
                    acc.setTimeWindow(value.getTimeWindow());
                }
                acc.setCount(acc.getCount() + 1);
                acc.setDurationSum(acc.getDurationSum() + value.getDuration());
                if (value.getDuration() > acc.getDurationMax()) {
                    acc.setDurationMax(value.getDuration());
                }
                if (value.getStatus() >= 400) {
                    acc.setErrorCount(acc.getErrorCount() + 1);
                }
                return acc;
            }

            @Override
            public NginxLogStats getResult(NginxLogStats acc) {
                acc.setDurationAvg(acc.getCount() > 0 ? acc.getDurationSum() / acc.getCount() : 0);
                if (acc.getCount() != 0 && acc.getErrorCount() != 0) {
                    acc.setErrorRate((double) acc.getErrorCount() / (double) acc.getCount());
                }
                return acc;
            }

            @Override
            public NginxLogStats merge(NginxLogStats a, NginxLogStats b) {
                a.setCount(a.getCount() + b.getCount());
                a.setErrorCount(a.getErrorCount() + b.getErrorCount());
                a.setDurationSum(a.getDurationSum() + b.getDurationSum());
                a.setDurationAvg(a.getCount() > 0 ? a.getDurationSum() / a.getCount() : 0);
                if (a.getDurationMax() < b.getDurationMax()) {
                    a.setDurationMax(b.getDurationMax());
                }
                if (a.getCount() != 0 && a.getErrorCount() != 0) {
                    a.setErrorRate((double) a.getErrorCount() / (double) a.getCount());
                }
                return a;
            }
        });

        aggSgtream.addSink(new NginxLogStatsEsSink(esServers,null, true,7));

    }
}
