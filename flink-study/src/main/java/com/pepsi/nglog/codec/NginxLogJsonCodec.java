package com.pepsi.nglog.codec;

import com.pepsi.nglog.dto.RichNginxLog;
import com.pepsi.util.PepsiUtil;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-07-04 11:56
 * Description: No Description
 */
public class NginxLogJsonCodec implements KeyedDeserializationSchema<RichNginxLog>, SerializationSchema<RichNginxLog> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(NginxLogJsonCodec.class);

    protected ObjectMapper mapper = new ObjectMapper();

    public NginxLogJsonCodec() {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public RichNginxLog deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset) throws IOException {
        RichNginxLog m = null;
        if (message != null && message.length > 0) {
            try {
                m = mapper.readValue(message, RichNginxLog.class);
                if (!validate(m)) {
                    m = null;
                    logger.debug("Invalid Nginx access log");
                }
            } catch (Exception e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Fail to deserialize RichNginxLog", e);
                }
            }
        }
        return m;
    }

    @Override
    public boolean isEndOfStream(RichNginxLog nextElement) {
        return false;
    }

    @Override
    public byte[] serialize(RichNginxLog element) {
        byte[] result = null;
        try {
            result = mapper.writeValueAsBytes(element);
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Fail to serialize object");
            }
        }
        return result == null ? new byte[0] : result;
    }

    @Override
    public TypeInformation<RichNginxLog> getProducedType() {
        return TypeExtractor.getForClass(RichNginxLog.class);
    }

    public boolean validate(RichNginxLog data) {
        // 由于目前运维上报的数据格式比较混乱, 我们需要过滤这些数据
        return PepsiUtil.checkNonEmpty(data.getPath()) && PepsiUtil.checkNonEmpty(data.getDomain()) && data.getTimestamp() > 0;
    }
}
