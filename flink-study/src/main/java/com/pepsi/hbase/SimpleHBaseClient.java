package com.pepsi.hbase;

import org.hbase.async.Config;
import org.hbase.async.HBaseClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 23:04
 * Description: No Description
 */
public class SimpleHBaseClient implements Closeable {
    private AtomicInteger state;
    private Config cfg;

    private HBaseClient hbase;

    public SimpleHBaseClient(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("params should not be empty");
        }
        if (!params.containsKey("hbase.zookeeper.quorum")) {
            throw new IllegalArgumentException("hbase.zookeeper.quorum is missing");
        }

        this.state = new AtomicInteger();

        Config cfg = new Config();
        for (Map.Entry<String, String> it : params.entrySet()) {
            cfg.overrideConfig(it.getKey(), it.getValue());
        }
        this.cfg = cfg;
    }

    public SimpleHBaseClient(Properties params) {
        this(params != null ?
                params.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()))
                : Collections.emptyMap());

    }

    public void start() throws IOException {
        if (state.compareAndSet(0, 1)) {
            hbase = new HBaseClient(cfg);
        }
    }

    @Override
    public void close() throws IOException {
        if (state.compareAndSet(1, 0)) {
            if (hbase != null) {
                try {
                    hbase.flush().join(1000 * 5);
                } catch (Exception ignored) {
                }
                try {
                    hbase.shutdown().join(1000 * 5);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public HBaseClient getHBase() {
        return hbase;
    }

    public PutCommand newPut() {
        return new PutCommand(this);
    }

    public <V> GetCommand<V> newGet(Function<byte[], V> valueDecoder) {
        return new GetCommand<>(this, valueDecoder);
    }
}
