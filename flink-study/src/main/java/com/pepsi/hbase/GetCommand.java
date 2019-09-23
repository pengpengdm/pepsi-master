package com.pepsi.hbase;

import com.pepsi.util.FlinkUtils;
import com.stumbleupon.async.Deferred;
import org.hbase.async.GetRequest;
import org.hbase.async.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;



public class GetCommand<V> {

    private static final Logger logger = LoggerFactory.getLogger(GetCommand.class);

    private SimpleHBaseClient client;
    private Function<byte[], V> valueDecoder;

    private boolean async = true;
    private long timeout = 1000L * 10L;

    private byte[] table;
    private byte[] key;
    private byte[] family;
    private List<byte[]> qualifiers;

    private Deferred<ArrayList<KeyValue>> deferred;

    public GetCommand(SimpleHBaseClient client, Function<byte[], V> valueDecoder) {
        if (client == null || valueDecoder == null) {
            throw new IllegalArgumentException("client or valueDecoder should not be empty");
        }
        this.client = client;
        this.valueDecoder = valueDecoder;
    }

    public CompletableFuture<List<V>> execute() throws Exception {
        if (FlinkUtils.checkNonEmpty(table) && FlinkUtils.checkNonEmpty(key)) {
            GetRequest req = new GetRequest(table, key);
            if (FlinkUtils.checkNonEmpty(family)) {
                req.family(family);
            }
            if (FlinkUtils.checkNonEmpty(qualifiers)) {
                req.qualifiers(FlinkUtils.toArray(qualifiers));
            }
            deferred = client.getHBase().get(req);
            if (async) {
                return CompletableFuture.supplyAsync(this::getResult);
            } else {
                List<V> data = getResult();
                return CompletableFuture.completedFuture(data);
            }
        }
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

    public GetCommand setAsync(boolean value) {
        this.async = value;
        return this;
    }

    public GetCommand setTimeout(long value) {
        if (value > 0) {
            this.timeout = value;
        }
        return this;
    }

    public GetCommand setTable(String value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.table = value.getBytes(FlinkUtils.getCharset());
        }
        return this;
    }

    public GetCommand setTable(byte[] value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.table = value;
        }
        return this;
    }

    public GetCommand setKey(String value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.key = value.getBytes(FlinkUtils.getCharset());
        }
        return this;
    }

    public GetCommand setKey(byte[] value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.key = value;
        }
        return this;
    }

    public GetCommand setFamily(String value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.family = value.getBytes(FlinkUtils.getCharset());
        }
        return this;
    }

    public GetCommand setFamily(byte[] value) {
        if (FlinkUtils.checkNonEmpty(value)) {
            this.family = value;
        }
        return this;
    }

    public GetCommand addQualifier(byte[] qualifier) {
        if (FlinkUtils.checkNonEmpty(qualifier)) {
            if (this.qualifiers == null) {
                this.qualifiers = new ArrayList<>();
            }
            this.qualifiers.add(qualifier);
        }
        return this;
    }

    public GetCommand addQualifier(String qualifier) {
        if (FlinkUtils.checkNonEmpty(qualifier)) {
            return addQualifier(qualifier.getBytes(FlinkUtils.getCharset()));
        }
        return this;
    }

    protected List<V> getResult() {
        if (deferred != null) {
            try {
                List<KeyValue> result = deferred.join(timeout);
                if (result != null && !result.isEmpty()) {
                    List<V> data = new ArrayList<>(result.size());
                    for (KeyValue kv : result) {
                        V val = valueDecoder.apply(kv.value());
                        if (val != null) {
                            data.add(val);
                        }
                    }
                    return data;
                }
            } catch (Exception e) {
                logger.warn("Fail to get HBase result.", e);
            }
        }
        return Collections.emptyList();
    }
}
