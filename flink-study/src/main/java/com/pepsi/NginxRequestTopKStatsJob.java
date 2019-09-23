package com.pepsi;

import com.pepsi.bean.Constant;
import com.pepsi.bean.RichNginxLog;
import com.pepsi.bean.codec.RichNginxLogCodec;
import com.pepsi.function.TopKReqSummaryAggregationFunction;
import com.pepsi.function.TopKReqSummaryHBaseSink;
import com.pepsi.function.TopKRequestSummary;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 23:30
 * Description: topN 请求统计.
 */
public class NginxRequestTopKStatsJob extends AbstractStreamJob{
    private Properties hbaseProps;

    public static void main(String[] args) throws Exception {
        NginxRequestTopKStatsJob job = new NginxRequestTopKStatsJob();
        job.init(args);
        job.execute();
    }

    @Override
    protected void postInit() throws Exception {
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        hbaseProps = loadConfig("hbase_cfg_path", 1024L * 100L);
    }

    @Override
    protected void preExecute() {
        int topk = config.getInteger("topk",10);
        int window = config.getInteger("window.size",60);
        String table = config.getString("hbase.nginx.table", Constant.HBASE_TABLE);
        String family = config.getString("hbase.nginx.family", Constant.HBASE_FAMILY);
        RichNginxLogCodec codec = new RichNginxLogCodec();
        FlinkKafkaConsumerBase<RichNginxLog> kafkaSource = createKafkaSource(codec);
        TopKReqSummaryAggregationFunction aggregationFunction= new TopKReqSummaryAggregationFunction(topk);
        TopKReqSummaryHBaseSink sink = new TopKReqSummaryHBaseSink(window * 1000, hbaseProps, table, family, true);
        DataStream<RichNginxLog> stream = env.addSource(kafkaSource);
        SingleOutputStreamOperator<TopKRequestSummary<RichNginxLog>> summaryStreamByPath = stream
                .map(new RichMapFunction<RichNginxLog, Tuple3<Integer, String, RichNginxLog>>() {
                    @Override
                    public Tuple3<Integer, String, RichNginxLog> map(RichNginxLog value) throws Exception {
                        return Tuple3.of(getRuntimeContext().getIndexOfThisSubtask(),value.getPath()+"|"+value.getAppName(),value);
                    }
                }).keyBy(0)
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .aggregate(aggregationFunction)
                .windowAll(TumblingEventTimeWindows.of(Time.seconds(window)))
                .reduce((a, b) -> {
                    a.merge(b);
                    return a;
                });
        summaryStreamByPath.addSink(sink);
//        summaryStreamByPath.print();
    }
}
