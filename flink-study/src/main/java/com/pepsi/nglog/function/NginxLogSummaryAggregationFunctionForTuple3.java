package com.pepsi.nglog.function;

import com.pepsi.nglog.dto.RichNginxLog;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-09 15:41
 * Description: No Description
 */
public class NginxLogSummaryAggregationFunctionForTuple3 implements AggregateFunction<Tuple3<Integer, String, RichNginxLog>,TopKWindowSummary<RichNginxLog>, TopKWindowSummary<RichNginxLog>> {

    private static final long serialVersionUID = 1L;

    private String type;

    private int topk;

    public NginxLogSummaryAggregationFunctionForTuple3(String type,int topk){
        this.topk = topk;
        this.type = type;
    }


    @Override
    public TopKWindowSummary<RichNginxLog> createAccumulator() {
        return new TopKWindowSummary<RichNginxLog>(topk);
    }

    @Override
    public TopKWindowSummary<RichNginxLog> add(Tuple3<Integer, String, RichNginxLog> value, TopKWindowSummary<RichNginxLog> accumulator) {
        accumulator.add(value.f1,value.f2);
        return accumulator;
    }

    @Override
    public TopKWindowSummary<RichNginxLog> getResult(TopKWindowSummary<RichNginxLog> accumulator) {
        return null;
    }

    @Override
    public TopKWindowSummary<RichNginxLog> merge(TopKWindowSummary<RichNginxLog> a, TopKWindowSummary<RichNginxLog> b) {
        return null;
    }
}
