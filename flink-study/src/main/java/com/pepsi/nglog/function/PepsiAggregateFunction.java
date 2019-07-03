package com.pepsi.nglog.function;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-19 22:11
 * Description: 聚合算子 Tuple2<Integer,Integer> sum 和 count
 */
public class PepsiAggregateFunction implements AggregateFunction<Tuple3<Integer,Integer, Date>,Integer,Integer> {


    @Override
    public Integer createAccumulator() {
        return 0;
    }

    @Override
    public Integer add(Tuple3<Integer, Integer, Date> tuple3, Integer integer) {
        integer +=tuple3.f0;
        return integer;
    }

    @Override
    public Integer getResult(Integer integer) {
        return integer;
    }

    @Override
    public Integer merge(Integer integer, Integer acc1) {
        return integer+acc1;
    }
}
