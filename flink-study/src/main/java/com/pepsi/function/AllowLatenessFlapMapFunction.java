package com.pepsi.function;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-21 13:54
 * Description: No Description
 */
public class AllowLatenessFlapMapFunction implements FlatMapFunction<String, Tuple2<String,Long>> {

    private static final Logger log = LoggerFactory.getLogger(AllowLatenessFlapMapFunction.class);

    @Override
    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
        log.info("nc String is {}",value);
        String[] splitStrs  = value.split(",");
        Tuple2<String,Long> tuple2 = new Tuple2<>(splitStrs[0],Long.valueOf(splitStrs[1]));
        out.collect(tuple2);
    }
}



