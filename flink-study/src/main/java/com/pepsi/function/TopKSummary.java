package com.pepsi.function;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.lucene.util.ArrayUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 对频率Top-K的元素进行统计(Frequency estimation and heavy hitters)<p>
 * <p>
 * 采用 Misra-Gries 算法, Refer to https://en.wikipedia.org/wiki/Misra-Gries_summary<p>
 * 其他算法如 SpaceSaving, 和本算法是等价的.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class TopKSummary<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Map<K, Stats<K, V>> repo;
    protected int capacity;

    public TopKSummary(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity should be positive.");
        }
        this.capacity = capacity;
        this.repo = new HashMap<>(capacity / 8);
    }

    /**
     * 对新元素以及集合内已有元素进行统计
     *
     * @param value 新元素
     * @return 0 表示该元素已经存在, 1 表示该元素是新加入的, 2 表示集合已满并被裁减, 3 表示非法元素
     */
    public int add(K key, V value) {
        Stats cnt = repo.get(key);
        if (cnt != null) {
            // 如果新元素存在, 对计数加1
            cnt.count++;
            return 0;
        } else if (repo.size() < capacity) {
            // 如果新元素不存在, 则加入
            // 注意 repo.size() 必需是 O(1) 操作
            repo.put(key, newStats(key, value));
            return 1;
        } else {
            // 如果新元素不存在, 并且集合满了, 对所有的计数减1, 如果计数变成0, 则删除
            updateAndRemoveInfrequentItems(1);
            return 2;
        }
    }

    /**
     * 合并2个统计的结果
     *
     * @param other 另一个统计结果
     */
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

    /**
     * 得到某个元素的次数的估算
     *
     * @param key 元素的key
     * @return 次数的估算
     */
    public Stats<K, V> get(K key) {
        return repo.get(key);
    }

    /**
     * 得到某个元素的统计值
     *
     * @param key 某个元素key
     * @return 统计值
     */
    public long getCount(K key) {
        Stats<K, V> stats = get(key);
        return stats != null ? stats.count : 0;
    }

    /**
     * 得到所有元素的统计值
     *
     * @param sorted 是否需要根据次数降序排列
     * @return 所有统计值
     */
    public List<Stats<K, V>> getAll(boolean sorted) {
        return sortStats(repo.values(), sorted);
    }

    public Set<K> getAllKeys() {
        return Collections.unmodifiableSet(repo.keySet());
    }

    /**
     * 精确统计各个元素出现的频度
     *
     * @param items  各个元素
     * @param sorted 是否需要根据次数降序排列
     * @return 统计值
     */
    public List<Stats<K, V>> getPreciseStats(Collection<Tuple2<K, V>> items, boolean sorted) {
        Map<K, Stats<K, V>> map = new HashMap<>();
        for (Tuple2<K, V> it : items) {
            K k = it.f0;
            if (k == null) {
                continue;
            }
            Stats<K, V> stats = map.get(k);
            if (stats == null) {
                stats = newStats(k, it.f1);
                map.put(k, stats);
            } else {
                stats.count++;
            }
        }
        return sortStats(map.values(), sorted);
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

    private List<Stats<K, V>> sortStats(Collection<Stats<K, V>> data, boolean sorted) {
        if (sorted) {
            List<Stats<K, V>> copy = new ArrayList<>(data);
            Collections.sort(copy, Stats::compareReversed);
            return copy;
        }
        return new ArrayList<>(data);
    }

    public static class Stats<K, V> {
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
