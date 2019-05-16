package com.pepsi.stream;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/17
 * describe: 通过socket模拟产生单词数据。滑动窗口计算
 */
public class SocketWindowWordCount {

    public static void main(String[] args) throws Exception {
        int port;
        try {
            ParameterTool parameterTool = ParameterTool.fromArgs(args);
            port = parameterTool.getInt("port");
        }catch (Exception e){
            System.err.println("No port set. use default port 9000--java");
            port = 9000;
        }
        StreamExecutionEnvironment env =StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource text = env.socketTextStream("localhost",port,"\n");
        text.print();
        env.execute("SocketWindowWordCount");
    }


}
