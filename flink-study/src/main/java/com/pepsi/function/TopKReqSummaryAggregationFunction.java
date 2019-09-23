package com.pepsi.function;

import com.pepsi.bean.RichNginxLog;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 23:39
 * Description: No Description
 */
public class TopKReqSummaryAggregationFunction implements AggregateFunction<Tuple3<Integer, String, RichNginxLog>, TopKRequestSummary<RichNginxLog>, TopKRequestSummary<RichNginxLog>> {

    private int topk;

    public TopKReqSummaryAggregationFunction(int topk) {
        this.topk = topk;
    }

    @Override
    public TopKRequestSummary<RichNginxLog> createAccumulator() {
        return new TopKRequestSummary<>(topk);
    }

    @Override
    public TopKRequestSummary<RichNginxLog> add(Tuple3<Integer, String, RichNginxLog> value, TopKRequestSummary<RichNginxLog> accumulator) {
        accumulator.add(value.f1, value.f2);
        return accumulator;
    }

    @Override
    public TopKRequestSummary<RichNginxLog> getResult(TopKRequestSummary<RichNginxLog> accumulator) {
        return accumulator;
    }

    @Override
    public TopKRequestSummary<RichNginxLog> merge(TopKRequestSummary<RichNginxLog> a, TopKRequestSummary<RichNginxLog> b) {
        a.merge(b);
        return a;
    }
}
