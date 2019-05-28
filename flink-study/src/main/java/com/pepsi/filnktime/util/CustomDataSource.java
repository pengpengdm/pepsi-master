package com.pepsi.filnktime.util;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-23 11:15
 * Description: 自定义数据源
 */

public class CustomDataSource implements SourceFunction<Tuple3<Integer,Integer,Date>> {

    private boolean isRunning=true;
    private int count = 1;

    @Override
    public void run(SourceContext<Tuple3<Integer, Integer, Date>> sourceContext) throws Exception {
        while (isRunning) {
            sourceContext.collect(new Tuple3<>(count, count%5, new Date()));
            count++;
            Thread.sleep(500);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

}
