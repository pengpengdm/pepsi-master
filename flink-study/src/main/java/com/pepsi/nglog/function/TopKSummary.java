package com.pepsi.nglog.function;

import org.apache.lucene.util.ArrayUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-09 15:11
 * Description: No Description
 */
public class TopKSummary<K,V> implements Serializable {
    private Map<K,Stats<K,V>> repo;
    private int capacity;

    public TopKSummary(int capacity){
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity should be positive.");
        }
        this.capacity = capacity;
        this.repo = new HashMap<>(capacity/8);
    }


    /***
     *
     * @param key
     * @param value
     * @return
     */
    public int add(K key,  V value) {
        Stats cnt = repo.get(key);
        if(cnt!=null){
            cnt.count++;
            return 0;
        }else if( repo.size()< capacity ){
            repo.put(key,newStats(key, value));
            return 1;
        }else {
            //新的并且，槽位满了。那么开始将所有的count -1， 如果计数变成0, 则删除
            updateAndRemoveInfrequentItems(1);
            return 2;
        }
    }

    /***
     * count --
     * @param pivotCount
     */
    private void updateAndRemoveInfrequentItems(long pivotCount) {
        Iterator<Map.Entry<K, Stats<K, V>>> it = repo.entrySet().iterator();
        while (it.hasNext()) {
            Stats c = it.next().getValue();
            c.count -= pivotCount;
            if (c.count <= 0) {
                it.remove();
            }
        }
    }

    /**
     * 产生新的统计对象 (允许子类overwrite)
     *
     * @param key   键值
     * @param value 新的元素
     * @return 新的统计对象
     */
    protected Stats newStats(K key, V value) {
        // Ignore value by default, sub-class can overwrite this behaviour
        return new Stats(key, 1);
    }


    /**
     * 得到所有元素的统计值
     *
     * @return 所有统计值
     */
    List<Stats<K, V>> getAll(boolean sorted) {
        return sortStats(repo.values(), sorted);
    }

    private List<Stats<K, V>> sortStats(Collection<Stats<K, V>> data, boolean sorted) {
        if (sorted) {
            List<Stats<K, V>> copy = new ArrayList<>(data);
            Collections.sort(copy, Stats::compareReversed);
            return copy;
        }
        return new ArrayList<>(data);
    }

    public void merge(TopKSummary<K, V> other) {
        // 首先合并. 相同key的计数相加, 新key则加入.
        // Refer to https://dl.acm.org/citation.cfm?id=2213562
        if (this == other || other == null) {
            return;
        }
        for (Stats<K, V> it : other.repo.values()) {
            Stats old = repo.putIfAbsent(it.key, it);
            if (old != null) {
                old.count += it.count;
            }
        }
        // 然后裁减去掉频度低的元素
        trim();
    }

    private void trim() {
        int k = capacity;
        int len = repo.size();
        if (len <= k) {
            return;
        }
        // 找到第k个最大的计数.
        // 使用 Quick Select 算法将数组分成2部分, [0, k)的计数都大于第k个计数.
        // Quick Select 要远远快于通过排序或者PriorityQueue的算法, 参考 TopKSummaryBenchmark 结果
        Stats[] merged = repo.values().toArray(new Stats[len]);
        ArrayUtil.select(merged, 0, merged.length, k, Stats::compareReversed);

        // 对所有的计数减去第k个计数, 如果计数变成0, 则删除
        updateAndRemoveInfrequentItems(merged[k].count);
    }


    public static class Stats<K,V>{
        protected K key;
        protected long count;
        protected V sample;

        public Stats(K key, long count) {
            this(key, count, null);
        }

        public Stats(K key, long count, V sample) {
            this.key = key;
            this.count = count;
            this.sample = sample;
        }

        public K getKey() {
            return key;
        }

        public long getCount() {
            return count;
        }

        public V getSample() {
            return sample;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Stats.class.getSimpleName() + "[", "]")
                    .add("key=" + key)
                    .add("count=" + count)
                    .add("sample=" + sample)
                    .toString();
        }

        public static int compareReversed(Stats o1, Stats o2) {
            return (int) (o2.count - o1.count);
        }
    }
}
