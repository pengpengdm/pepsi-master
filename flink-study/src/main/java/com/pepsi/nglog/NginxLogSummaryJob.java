package com.pepsi.nglog;

import com.pepsi.filnktime.util.NgLogDataSource;
import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.nglog.function.NginxLogStatsEsSink;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-18 15:51
 * Description: 使用nginx 日志筛选
 */

public class NginxLogSummaryJob extends AbstractStreamingJob {


    public static void main(String[] args) throws Exception {
        NginxLogSummaryJob job = new NginxLogSummaryJob();
        job.init(args);
        job.execute();
    }

    @Override
    protected void preExecute() {

        DataStreamSource<RichNginxLog> baseStream = env.addSource(new NgLogDataSource());

        String esServers = cfg.getString("es_nginx_servers", null);

        DataStream<RichNginxLog> processStream = baseStream.keyBy((KeySelector<RichNginxLog, String>) RichNginxLog::getPath)
                .timeWindow(Time.seconds(5))
                .process(new ProcessWindowFunction<RichNginxLog, RichNginxLog, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<RichNginxLog> iterable, Collector<RichNginxLog> collector) throws Exception {
                       for(RichNginxLog nginxLog:iterable) {
                           if(nginxLog.getPath().contains("09")) {
                               collector.collect(nginxLog);
                           }
                       }
                    }
                });
        if (debug) {
            processStream.print();
        }
        if (!dryrun) {
            processStream .addSink(new NginxLogStatsEsSink(esServers,null, true,7));
        }
    }
}
