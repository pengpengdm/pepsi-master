package com.pepsi.function;

import com.pepsi.bean.NginxLogStats;
import com.pepsi.bean.RichNginxLog;
import com.pepsi.util.PercentileSummaryTdigest;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 17:03
 * Description: No Description
 */
public class NginxLogAggregateFunction implements AggregateFunction<RichNginxLog, NginxLogStats, NginxLogStats> {

    @Override
    public NginxLogStats createAccumulator() {
        return new NginxLogStats();
    }

    @Override
    public NginxLogStats add(RichNginxLog value, NginxLogStats accumulator) {
        if(StringUtils.isNullOrWhitespaceOnly(accumulator.getAppName())){
            accumulator.setAppName(value.getAppName());
            accumulator.setDomain(value.getDomain());
            accumulator.setPath(value.getPath());
            accumulator.setUpstreamAddr(value.getUpstream());
            accumulator.setSummary(new PercentileSummaryTdigest(100));
        }
        accumulator.setCount(accumulator.getCount()+1);
        accumulator.setDurationSum(value.getDuration()+accumulator.getDurationSum());
        PercentileSummary summary = accumulator.getSummary();
        if (summary != null) {
            summary.add(value.getDuration());
        }
        return accumulator;
    }

    @Override
    public NginxLogStats getResult(NginxLogStats accumulator) {
        //耗时 99 线
        PercentileSummary summary = accumulator.getSummary();
        if (summary != null) {
            Double p99 = summary.getPercentile(0.99);
            if (p99 != null) {
                accumulator.setDurationP99(p99.longValue());
            }
            // 清理数据
            accumulator.setSummary(null);
        }
        return accumulator;
    }

    @Override
    public NginxLogStats merge(NginxLogStats a, NginxLogStats b) {
        return a;
    }
}
