package com.pepsi.filnktime.util;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-24 17:40
 * Description: watermark 生成。
 */
public class TimeLagWatermarkGenerator implements AssignerWithPeriodicWatermarks<Tuple2<Integer, Date>> {

    //延迟统计
    private final long maxTimeLag = 60*1000;

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(System.currentTimeMillis() - maxTimeLag);
    }

    @Override
    public long extractTimestamp(Tuple2<Integer, Date> tuple2, long l) {
        return tuple2.f1.getTime();
    }
}
