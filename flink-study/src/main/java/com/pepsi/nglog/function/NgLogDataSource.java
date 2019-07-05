package com.pepsi.nglog.function;

import com.pepsi.nglog.dto.RichNginxLog;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-03 20:55
 * Description: No Description
 */
public class NgLogDataSource implements SourceFunction<RichNginxLog> {

    private boolean running = true;

    private int count;
    @Override
    public void run(SourceContext<RichNginxLog> sourceContext) throws Exception {
        while(running){
            Thread.sleep(500);
            RichNginxLog nginxLog = new RichNginxLog();
            nginxLog.setUpstream("i am uptream");
            nginxLog.setStatus(200);
            nginxLog.setDuration(10);
            nginxLog.setPath("/pepsi/si/console0"+(count%9));
            nginxLog.setDomain("baidu.com");
//            nginxLog.setTimestamp(new Date().getTime());
            sourceContext.collect(nginxLog);
            count++;
        }
    }

    @Override
    public void cancel() {
        running=false;
    }
}
