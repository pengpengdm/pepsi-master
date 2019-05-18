package com.pepsi.stream;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-18 14:33
 * Description: checkout point 观察
 */
public class StreamCheckPoint {


    public static void main(String[] args) {
        //获取flink的运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

         env.socketTextStream("",9000);








    }



}
