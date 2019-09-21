package com.pepsi.function;

import com.pepsi.util.FlinkUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-21 13:49
 * Description: No Description
 */
public class AllowLatenessAggregationFunction implements AggregateFunction<Tuple2<String,Long>, Tuple3<String,String,Integer>,Tuple3<String,String,Integer>> {
    @Override
    public Tuple3<String, String, Integer> createAccumulator() {
        return new Tuple3<>();
    }

    @Override
    public Tuple3<String, String, Integer> add(Tuple2<String, Long> value, Tuple3<String, String, Integer> acc) {
        if(StringUtils.isNullOrWhitespaceOnly(acc.f0)){
            acc.setField(value.f0,0);
            acc.setField(FlinkUtils.formatTimestamp(value.f1),1);
            acc.setField(0,2);
        }
        acc.setField(acc.f2+1,2);
        return acc;
    }

    @Override
    public Tuple3<String, String, Integer> getResult(Tuple3<String, String, Integer> accumulator) {
        return accumulator;
    }

    @Override
    public Tuple3<String, String, Integer> merge(Tuple3<String, String, Integer> a, Tuple3<String, String, Integer> b) {
        return a;
    }
}
