package com.pepsi.util;

import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.NetworkModule;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.Netty4Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 10:24
 * Description: No Description
 */
public class EsClient implements Closeable, Serializable {

    private static final long serialVersionUID = -1L;

    private static final Logger logger = LoggerFactory.getLogger(EsClient.class);

    private Map<String, String> params;
    private String servers;

    private int batchCount = 1000;
    private long batchSize = 5L * 1024L * 1024L;
    private long batchInterval = 1000L * 10L;
    private int batchConcurrentRequests = 10;
    private long batchBackoffDelay = 1000;
    private int batchRetries = 1;

    private boolean compactEnabled;

    private boolean enableBulkProcessor = true;
    private long closeTimeout = 5000;

    private AtomicInteger state;

    private TransportClient es;
    private BulkProcessor processor;

    public EsClient(String servers, Map<String, String> params, boolean enableBulkProcessor) {
        if (servers == null | servers.isEmpty()) {
            throw new IllegalArgumentException("servers should not be empty.");
        }
        this.servers = servers;
        this.params = params != null ? params : Collections.emptyMap();
        this.enableBulkProcessor = enableBulkProcessor;
        this.state = new AtomicInteger(0);
    }

    public EsClient(String servers, Properties params) {
        this(servers,
                params != null ?
                        params.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()))
                        : Collections.emptyMap(),
                true);
    }

    public EsClient(String servers) {
        this(servers, Collections.emptyMap(), true);
    }

    public void start() throws IOException {
        if (state.compareAndSet(0, 1)) {
            // 需要屏蔽, 否则和其他 netty 的 caller 冲突
            System.setProperty("es.set.netty.runtime.available.processors", "false");

            // 自定义属性需要覆盖默认属性, 所以放在最后
            Settings settings = Settings.builder()
                    .put(NetworkModule.HTTP_TYPE_KEY, Netty4Plugin.NETTY_HTTP_TRANSPORT_NAME)
                    .put(NetworkModule.TRANSPORT_TYPE_KEY, Netty4Plugin.NETTY_TRANSPORT_NAME)
                    .put("cluster.name", "elasticsearch")
                    .put("client.transport.sniff", "true")
                    .put("client.transport.ignore_cluster_name", "true")
                    .put("client.transport.ping_timeout", "15s")
                    .put("client.transport.nodes_sampler_interval", "15s")
                    .put(this.params)
                    .build();

            TransportClient transportClient = new PreBuiltTransportClient(settings);
            for (InetSocketAddress addr : FlinkUtils.parseNetworkAddress(servers, 9200)) {
                transportClient.addTransportAddress(new InetSocketTransportAddress(addr));
            }

            // verify that we actually are connected to a cluster
            if (transportClient.connectedNodes().isEmpty()) {
                try {
                    if (transportClient != null) {
                        transportClient.close();
                    }
                } catch (Throwable ignored) {
                }

                throw new IOException("Fail to connect to Elasticsearch.");
            }

            if (logger.isInfoEnabled()) {
                logger.info("Connect to Elasticsearch: " + transportClient.connectedNodes());
            }

            es = transportClient;

            if (enableBulkProcessor) {
                processor = createBulkProcessor();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (state.compareAndSet(1, 0)) {
            if (processor != null) {
                try {
                    processor.awaitClose(closeTimeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException exc) {
                    Thread.currentThread().interrupt();
                }
            }
            if (es != null) {
                try {
                    es.close();
                } catch (Exception e) {
                    logger.warn("Fail to close EsClient", e);
                }
                es = null;
            }
        }
    }

    public EsClient doIndex(String index, String type, String value) {
        processor.add(es.prepareIndex(index, type).setSource(value, XContentType.JSON).request());
        return this;
    }

    public EsClient doIndex(String index, String type, XContentBuilder builder) {
        processor.add(es.prepareIndex(index, type).setSource(builder).request());
        return this;
    }

    public <T> List<T> get(String index, QueryBuilder query, int from, int size, long timeout, Function<String, T> processor) {
        List<T> result = Collections.emptyList();
        try {
            SearchRequestBuilder builder = es.prepareSearch(index).setFrom(from).setSize(size);
            if (query != null) {
                builder.setQuery(query);
            }
            SearchResponse resp = builder.get(TimeValue.timeValueMillis(timeout));

            SearchHit[] hits = resp.getHits().getHits();
            if (hits != null && hits.length > 0) {
                result = new ArrayList<>(hits.length);
                for (SearchHit hit : hits) {
                    T obj = processor.apply(hit.getSourceAsString());
                    if (obj != null) {
                        result.add(obj);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Fail to search ES", e);
        }
        return result;
    }

    public XContentBuilder newIndexBuilder() {
        try {
            return compactEnabled ? XContentFactory.smileBuilder() : XContentFactory.jsonBuilder();
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Fail to create XContentBuilder.", e);
            }
        }
        return null;
    }

    public void flush() {
        processor.flush();
    }

    public EsClient setBatchCount(int value) {
        if (value > 0) {
            this.batchCount = value;
        }

        return this;
    }

    public EsClient setBatchSize(long value) {
        if (value > 0) {
            this.batchSize = value;
        }
        return this;
    }

    public EsClient setBatchInterval(long value) {
        if (value > 0) {
            this.batchInterval = value;
        }
        return this;
    }

    public EsClient setBatchConcurrentRequests(int value) {
        if (value > 0) {
            this.batchConcurrentRequests = value;
        }
        return this;
    }

    public EsClient setBatchBackoffDelay(long value) {
        if (value > 0) {
            this.batchBackoffDelay = value;
        }
        return this;
    }

    public EsClient setBatchRetries(int value) {
        if (value > 0) {
            this.batchRetries = value;
        }
        return this;
    }

    public EsClient setCompactEnabled(boolean value) {
        this.compactEnabled = value;
        return this;
    }

    public EsClient setCloseTimeout(long value) {
        if (value >= 0) {
            this.closeTimeout = value;
        }
        return this;
    }

    private BulkProcessor createBulkProcessor() {
        return BulkProcessor.builder(
                es,
                new InternalListener())
                .setBulkActions(batchCount)
                .setBulkSize(new ByteSizeValue(batchSize, ByteSizeUnit.BYTES))
                .setFlushInterval(TimeValue.timeValueMillis(batchInterval))
                .setConcurrentRequests(batchConcurrentRequests)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(batchBackoffDelay), batchRetries))
                .build();
    }


    private class InternalListener implements BulkProcessor.Listener {

        @Override
        public void beforeBulk(long executionId, BulkRequest request) {
            if (logger.isDebugEnabled()) {
                logger.debug("Before ES bulk request: " + executionId);
            }
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
            if (response.hasFailures()) {
                if (logger.isWarnEnabled()) {
                    logger.warn("ES bulk request has error: " + executionId
                            + "; rc = " + response.hasFailures()
                            + "; duration = " + response.getTookInMillis()
                            + "; detail = " + response.buildFailureMessage());
                }
            } else if (logger.isDebugEnabled()) {
                logger.debug("ES bulk request is completed: " + executionId
                        + "; rc = " + response.hasFailures()
                        + "; duration = " + response.getTookInMillis());
            }
        }

        @Override
        public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            if (logger.isWarnEnabled()) {
                logger.warn("ES bulk request has failed: " + executionId, failure);
            }
        }
    }

}
