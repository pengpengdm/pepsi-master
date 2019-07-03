package com.pepsi.nglog.function;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-21 11:28
 * Description: No Description
 */
public class DataRichMapFuntion extends RichMapFunction<Tuple2<Integer,String>,Tuple2<Integer,String>> {


    @Override
    public Tuple2<Integer, String> map(Tuple2<Integer, String> tuple2) throws Exception {
        return null;
    }
}
