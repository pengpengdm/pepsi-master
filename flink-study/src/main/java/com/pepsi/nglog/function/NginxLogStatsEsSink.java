package com.pepsi.nglog.function;

import com.pepsi.client.EsClient;
import com.pepsi.nglog.dto.NginxLogStats;
import com.pepsi.util.PepsiUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-08 12:03
 * Description: nglog 统计后 sink 到ES
 */
public class NginxLogStatsEsSink extends RichSinkFunction<NginxLogStats> {

    private String servers;
    private Properties props;
    private boolean shared;
    private Long rolloverMs;

    private static final AtomicReference<EsClient> sharedRef = new AtomicReference<>();
    private AtomicReference<EsClient> ref;


    public NginxLogStatsEsSink(String servers, Properties props, boolean shared, long rolloverDay) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("servers should not be empty.");
        }
        if (rolloverDay < 1) {
            throw new IllegalArgumentException("rolloverDay should be positive");
        }
        this.servers = servers;
        this.props = props;
        this.shared = shared;
        this.rolloverMs = 1000L * 60 * 60 * 24 * rolloverDay;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        this.ref = shared ? sharedRef : new AtomicReference<>();
        if (ref.get() == null) {
            EsClient client = new EsClient(servers);
            if (ref.compareAndSet(null, client)) {
                client.start();
            }
        }
        super.open(parameters);
    }

    @Override
    public void invoke(NginxLogStats value, Context context) throws Exception {
        EsClient client = ref.get();
        if (client != null) {
            try {
                XContentBuilder builder = client.newIndexBuilder();
                builder.startObject();
                builder.field("timestamp", value.getTimestamp());
                builder.field("status", value.getStatus());
                builder.field("domain",value.getDomain());
                builder.field("count",value.getCount());
                builder.field("errorRate",value.getErrorRate());
                builder.field("durationP99",value.getDurationP99());
                if (PepsiUtil.checkNonEmpty(value.getUrlPath())) {
                    builder.field("urlPath", value.getUrlPath());
                }
                builder.endObject();
                long ts = value.getTimestamp();
                ts -= ts % rolloverMs;
                client.doIndex("pepsinginx-" + ts, "nginx", builder);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
        EsClient client = ref.get();
        if (client != null) {
            if (ref.compareAndSet(client, null)) {
                client.close();
            }
        }
    }


}