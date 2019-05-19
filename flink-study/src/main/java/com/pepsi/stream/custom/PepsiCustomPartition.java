package com.pepsi.stream.custom;

import org.apache.flink.api.common.functions.Partitioner;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/05/19
 * describe: 自定义分区。分区的意思通过分区器来精确得控制数据流向
 */
public class PepsiCustomPartition implements Partitioner<String> {

    @Override
    public int partition(String key, int numPartitions) {
        System.out.println("分区总数："+numPartitions);
        if(key.contains("pepsi")){
            return 0;
        }else{
            return 1;
        }

    }
}
