package com.pepsi.filnktime.util;

import com.pepsi.util.DateFormatUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-24 14:29
 * Description: 自定义告警源带时间戳.
 */
public class CustomDataSourceWithTimeStamp implements SourceFunction<Tuple2<Integer, Date>> {

    private boolean isRunnning=true;
    private int num = 1;

    String[] data = new String[]{"2019-05-26 18:28:00","2019-05-26 18:28:00","2019-05-26 18:27:00","2019-05-26 18:28:00","2019-05-26 18:29:00"};

    @Override
    public void run(SourceContext<Tuple2<Integer, Date>> sourceContext) throws Exception {
        while(isRunnning){
            Tuple2<Integer,Date> tuple2 = new Tuple2<>(num%5, DateFormatUtil.parseDate(data[num%5],DateFormatUtil.YYYY_MM_SS_HH_MM_SS));
            sourceContext.collectWithTimestamp(tuple2,tuple2.f1.getTime());
            sourceContext.emitWatermark(new Watermark(tuple2.f1.getTime()));
            num++;
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunnning=false;
    }



}
