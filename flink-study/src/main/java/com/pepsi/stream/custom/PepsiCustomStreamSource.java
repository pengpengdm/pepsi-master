package com.pepsi.stream.custom;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/19
 * describe:自定义 source，实现 SourceFunction 接口即可
 */
public class PepsiCustomStreamSource implements SourceFunction<Long> {
    private long count = 1L;

    private boolean isRunning = true;

    @Override
    public void run(SourceContext<Long> context) throws Exception {
        while (isRunning){
            context.collect(count);
            count++;
            //每秒产生一条数据
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunning=false;
    }
}
