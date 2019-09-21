package com.pepsi.bean.codec;

import com.pepsi.bean.RichNginxLog;
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
 * Date: 2019-09-20 14:37
 * Description: No Description
 */
public class RichNginxLogCodec implements KeyedDeserializationSchema<RichNginxLog>, SerializationSchema<RichNginxLog> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(RichNginxLogCodec.class);

    private ObjectMapper mapper = new ObjectMapper();

    public RichNginxLogCodec(){
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public byte[] serialize(RichNginxLog element) {
        byte[] results = null;
        try{
            results = mapper.writeValueAsBytes(element);
        }catch (Exception e){

        }
        return results;
    }

    @Override
    public RichNginxLog deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset) throws IOException {
        RichNginxLog m = null;
        if(message != null && message.length>0){
            try {
                m = mapper.readValue(message, RichNginxLog.class);
            }catch (Exception e){
                if (logger.isWarnEnabled()) {
                    logger.warn("Fail to deserialize IotErrorLog", e);
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
    public TypeInformation<RichNginxLog> getProducedType() {
        return TypeExtractor.getForClass(RichNginxLog.class);
    }
}
