package com.pepsi.nglog.function;

import com.pepsi.client.EsClient;
import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.util.PepsiUtil;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-01 16:22
 * Description: es sink
 */
public class NginxLogEsSink extends RichSinkFunction<RichNginxLog> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NginxLogEsSink.class);

    private String servers;
    private Properties props;
    private boolean shared;
    private long rolloverMs;

    private static final AtomicReference<EsClient> sharedRef = new AtomicReference<>();
    private AtomicReference<EsClient> ref;

    public NginxLogEsSink(String servers, Properties props, boolean shared, long rolloverDay) {
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
        super.open(parameters);

        this.ref = shared ? sharedRef : new AtomicReference<>();

        if (ref.get() == null) {
            EsClient client = new EsClient(servers);
            if (ref.compareAndSet(null, client)) {
                client.start();
            }
        }
    }

    @Override
    public void close() throws Exception {
        EsClient client = ref.get();
        if (client != null) {
            if (ref.compareAndSet(client, null)) {
                client.close();
            }
        }
        super.close();
    }

    @Override
    public void invoke(RichNginxLog value, Context context) throws Exception {
        EsClient client = ref.get();
        if (client != null) {
            try {

                XContentBuilder builder = client.newIndexBuilder();
                builder.startObject();

                builder.field("timestamp", value.getTimestamp());
                builder.field("status", value.getStatus());
                builder.field("domain",value.getDomain());
                if (PepsiUtil.checkNonEmpty(value.getPath())) {
                    builder.field("urlPath", value.getPath());
                }
                if (PepsiUtil.checkNonEmpty(value.getUpstream())) {
                    builder.field("upstreamAddr", value.getUpstream());
                }

                builder.endObject();
                long ts = value.getTimestamp();
                ts -= ts % rolloverMs;
                client.doIndex("testnginx-" + ts, "nginx", builder);
            } catch (Exception e) {

            }
        }
    }



}
