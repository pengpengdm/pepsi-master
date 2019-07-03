package com.pepsi.util;

import javafx.util.Pair;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.base.Preconditions;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-04 10:26
 * Description: No Description
 */
public class WeightRandom<K,V extends Number> {
    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public WeightRandom(List<Pair<K, V>> list) {
        for (Pair<K, V> pair : list) {
            double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey().doubleValue();//统一转为double
            this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());//权重累加
        }
    }

    public K random() {
        double randomWeight = this.weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
        return this.weightMap.get(tailMap.firstKey());
    }

    public static Integer randomNum(){
        Pair<Integer,Double> pair01 = new Pair<>(1000,3.0);
        Pair<Integer,Double> pair02 = new Pair<>(300,0.5);
        Pair<Integer,Double> pair03 = new Pair<>(2020,1.0);
        Pair<Integer,Double> pair04 = new Pair<>(4000,2.5);
        Pair<Integer,Double> pair05 = new Pair<>(3456,3.0);

        List<Pair<Integer,Double>> list = new ArrayList<>();
        list.add(pair01);
        list.add(pair02);
        list.add(pair03);
        list.add(pair04);
        list.add(pair05);
        WeightRandom weightRandom = new WeightRandom<>(list);
        return (Integer) weightRandom.random();
    }
}

