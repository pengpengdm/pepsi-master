package com.pepsi.nglog.function;

import com.pepsi.nglog.dto.RichNginxLog;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-09 15:48
 * Description: No Description
 */
public class NginxLogSummaryWindowFunction extends ProcessWindowFunction<TopKWindowSummary<RichNginxLog>, TopKWindowSummary<RichNginxLog>, Integer, TimeWindow> {
    @Override
    public void process(Integer integer, Context context, Iterable<TopKWindowSummary<RichNginxLog>> elements, Collector<TopKWindowSummary<RichNginxLog>> out) throws Exception {
        TimeWindow win = context.window();
        for (TopKWindowSummary<RichNginxLog> it:elements ) {
            it.setTimestamp(win.getStart());
            it.setTimeWindow(win.getEnd() - win.getStart());
            out.collect(it);
        }
    }
}
