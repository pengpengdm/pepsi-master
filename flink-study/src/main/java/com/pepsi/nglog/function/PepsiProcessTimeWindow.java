package com.pepsi.nglog.function;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-20 18:19
 * Description: No Description
 */
public class PepsiProcessTimeWindow extends ProcessWindowFunction<Integer, Integer, Tuple, TimeWindow> {


    @Override
    public void process(Tuple tuple3, Context context, Iterable<Integer> iterable, Collector<Integer> collector) throws Exception {
        System.out.println(new Date(context.currentProcessingTime()));
        for (Integer data:iterable) {
            collector.collect(data);
        }
    }
}
