package com.pepsi.filnktime.util;

import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.util.WeightRandom;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-21 11:35
 * Description: 自定义Tuple2数据源
 */
public class NgLogDataSource implements SourceFunction<RichNginxLog> {

    private boolean running = true;

    @Override
    public void run(SourceContext<RichNginxLog> sourceContext) throws Exception {
        int count = 1;
        while (running) {
            Thread.sleep(500);
            RichNginxLog nginxLog = new RichNginxLog();
            nginxLog.setPath("/pepsi/demo0"+count%10);
            nginxLog.setDomain("www.baidu.com");
            nginxLog.setDuration(WeightRandom.randomNum());
            nginxLog.setStatus(200);
            nginxLog.setUpstream("10.200.25.62:5000");
            nginxLog.setTimestamp(new Date().getTime());
            sourceContext.collect(nginxLog);
            count++;
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
