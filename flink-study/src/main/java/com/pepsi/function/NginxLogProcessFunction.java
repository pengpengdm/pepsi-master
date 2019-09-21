package com.pepsi.function;

import com.pepsi.bean.RichNginxLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 15:18
 * Description: No Description
 */
public class NginxLogProcessFunction extends ProcessFunction<RichNginxLog,RichNginxLog> implements Serializable {

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void processElement(RichNginxLog value, Context ctx, Collector<RichNginxLog> out) throws Exception {
        //todo 逻辑处理判断是是否是错误请求、慢URL等
        out.collect(value);
    }
}
