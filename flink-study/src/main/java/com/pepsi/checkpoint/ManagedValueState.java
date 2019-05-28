package com.pepsi.checkpoint;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-05-28 11:36
 * Description: 基于键值状态 ValueState 的托管
 */
public class ManagedValueState extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>{

    private transient ValueState<Tuple2<Long, Long>> sum;

    @Override
    public void flatMap(Tuple2<Long, Long> input, Collector<Tuple2<Long, Long>> out) throws Exception {
        // 存取状态值
        Tuple2<Long, Long> currentSum = sum.value();
        // 更新计数值
        currentSum.f0 += 1;

        // 将输入的元组值累计到第二项中
        currentSum.f1 += input.f1;

        // 更新状态
        sum.update(currentSum);

        // 如果计数值达到2，计算平均数，清除状态值
        if (currentSum.f0 >= 2) {
            out.collect(new Tuple2<>(input.f0, currentSum.f1 / currentSum.f0));
            sum.clear();
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Tuple2<Long,Long>> descriptor = new ValueStateDescriptor<>("average",
                TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}),Tuple2.of(0L, 0L));
        sum = getRuntimeContext().getState(descriptor);
    }

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.fromElements(Tuple2.of(1L, 3L), Tuple2.of(1L, 5L), Tuple2.of(1L, 7L), Tuple2.of(1L, 4L), Tuple2.of(1L, 2L))
                .keyBy(0)
                .flatMap(new ManagedValueState())
                .print();

        env.execute("ManagedValueState");
    }



}
