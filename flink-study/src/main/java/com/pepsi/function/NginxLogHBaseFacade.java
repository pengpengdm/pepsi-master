package com.pepsi.function;

import com.pepsi.bean.Constant;
import com.pepsi.bean.RequestTopKSummary;
import com.pepsi.hbase.SimpleHBaseClient;
import com.pepsi.util.FlinkUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class NginxLogHBaseFacade {

    private static final Logger logger = LoggerFactory.getLogger(NginxLogHBaseFacade.class);

    private SimpleHBaseClient client;
    private byte[] table;
    private byte[] family;
    private long timeWindow;

    private ObjectMapper mapper;

    protected byte[] defaultQualifier;

    public NginxLogHBaseFacade(SimpleHBaseClient client, byte[] table, byte[] family, long timeWindow) {
        this.client = client;
        this.table = table;
        this.family = family;
        this.timeWindow = timeWindow;
        this.defaultQualifier = Long.toString(timeWindow).getBytes(StandardCharsets.UTF_8);

        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public SimpleHBaseClient getClient() {
        return client;
    }

    public void setClient(SimpleHBaseClient client) {
        this.client = client;
    }

    public void writeSummary(RequestTopKSummary data, long timestamp, long timeout) throws Exception {
        String key = makeKey(null, timestamp);
        client.newPut().setTable(table).setFamily(family).setKey(key)
                .addData(defaultQualifier, serialize(data))
                .execute(timeout);
    }

    public RequestTopKSummary readSummary(String type, long timestamp, long timeout) throws Exception {
        String key = makeKey(type, timestamp);
        CompletableFuture<List<RequestTopKSummary>> fut = client.newGet(this::deserialize).setTimeout(timeout)
                .setTable(table).setFamily(family)
                .setKey(key).execute();
        List<RequestTopKSummary> results = fut.get(timeout, TimeUnit.MILLISECONDS);
        if (results != null && !results.isEmpty()) {
            if (results.size() <= 1) {
                return results.get(0);
            }

            // 合并数据
            RequestTopKSummary summary = results.get(0);
            Set<String> keys = summary.getKeys();
            if (keys == null) {
                keys = new HashSet<>();
                summary.setKeys(keys);
            }
            for (RequestTopKSummary it : results) {
                if (summary == it) {
                    continue;
                }
                if (it.getKeys() != null) {
                    keys.addAll(it.getKeys());
                }
                if (FlinkUtils.checkNonEmpty(it.getType())) {
                    summary.setType(it.getType());
                }
            }
            return summary;

        }
        return null;
    }

    protected String makeKey(String type, long timestamp) {
        long idx = timestamp - timestamp % timeWindow;
        int salt = (int) idx % Constant.HBASE_ROWKEY_SALT_MOD;

        StringBuilder sb = new StringBuilder(24).append(Integer.toHexString(salt));

        if (type == null) {
            type = Constant.DEFAULT_HBASE_ROWKEY_PREFIX;
        }
        if (!type.startsWith("_")) {
            sb.append('_');
        }
        sb.append(type);
        if (!type.endsWith("_")) {
            sb.append('_');
        }
        sb.append(idx);
        return sb.toString();
    }

    private RequestTopKSummary deserialize(byte[] data) {
        if (data != null && data.length > 0) {
            try {
                return mapper.readValue(data, RequestTopKSummary.class);
            } catch (Exception e) {
                logger.warn("Fail to deserialize NginxLogSummary", e);
            }
        }
        return null;
    }

    private byte[] serialize(RequestTopKSummary data) {
        byte[] result = null;
        try {
            result = mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            logger.warn("Fail to serialize NginxLogSummary");
        }
        return result == null ? new byte[0] : result;
    }
}
