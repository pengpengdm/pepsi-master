package com.pepsi.stream.sink;

import com.pepsi.stream.custom.PepsiCustomStreamSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/19
 * describe: sink 到文件
 */
public class StreamToFile {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Long> source =  env.addSource(new PepsiCustomStreamSource());

        DataStream<String> data= source.map(new MapFunction<Long, String>() {
            @Override
            public String map(Long aLong) throws Exception {
                System.out.println("pepsi is cool" +aLong);
                return "pepsi is cool" +aLong;
            }
        });

//       StreamingFileSink<String> sink = StreamingFileSink
//                .forRowFormat(new Path("/Users/pengjian/work/workspace/pepsi/pepsi-master/"), new SimpleStringEncoder<String>("UTF-8"))
//                .build();
        data.writeAsText("/Users/pengjian/work/workspace/pepsi/pepsi-master");
        env.execute("StreamToFile");
    }


}
