package com.pepsi.function;

import com.pepsi.bean.RequestTopKSummary;
import com.pepsi.bean.RichNginxLog;
import com.pepsi.hbase.SimpleHBaseClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 23:46
 * Description: No Description
 */
public class TopKReqSummaryHBaseSink extends RichSinkFunction<TopKRequestSummary<RichNginxLog>> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(TopKReqSummaryHBaseSink.class);

    private long timeWindow;
    private Properties props;
    private byte[] table;
    private byte[] family;
    private boolean shared;

    private static final AtomicReference<SimpleHBaseClient> sharedRef = new AtomicReference<>();
    private AtomicReference<SimpleHBaseClient> ref;

    private NginxLogHBaseFacade facade;

    public TopKReqSummaryHBaseSink(long timeWindow, Properties props, String table, String family, boolean shared) {
        if (timeWindow < 0) {
            throw new IllegalArgumentException("timeWindow should be positive.");
        }
        if (props == null || props.isEmpty()) {
            throw new IllegalArgumentException("props should not be empty.");
        }
        if (table == null || table.isEmpty() || family == null || family.isEmpty()) {
            throw new IllegalArgumentException("table and family should not be empty.");
        }
        this.timeWindow = timeWindow;
        this.props = props;
        this.shared = shared;
        this.table = table.getBytes(StandardCharsets.UTF_8);
        this.family = family.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        this.ref = shared ? sharedRef : new AtomicReference<>();
        if (ref.get() == null) {
            SimpleHBaseClient client = new SimpleHBaseClient(props);
            if (ref.compareAndSet(null, client)) {
                client.start();
            }
        }
        facade = new NginxLogHBaseFacade(null, table, family, timeWindow);
    }

    @Override
    public void close() throws Exception {
        facade = null;
        SimpleHBaseClient client = ref.get();
        if (client != null) {
            if (ref.compareAndSet(client, null)) {
                client.close();
            }
        }
        super.close();
    }

    @Override
    public void invoke(TopKRequestSummary<RichNginxLog> value, Context context) throws Exception {
        SimpleHBaseClient client = ref.get();
        if (client != null) {
            facade.setClient(client);
            try {
                RequestTopKSummary summary = new RequestTopKSummary();
                summary.setType(value.getType());
                summary.setKeys(value.getAllKeys());
                facade.writeSummary(summary, value.getTimestamp(), -1);
            } catch (Exception e) {
                logger.warn("Fail to write NginxLogSummary to HBase.", e);
            }
        }
    }
}
