package com.pepsi.nglog;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-18 15:50
 * Description: No Description
 */
public abstract class AbstractStreamingJob implements Serializable {

    protected Configuration cfg;

    protected Properties kafkaConsumerCfg;

    protected StreamExecutionEnvironment env;

    protected boolean debug;
    protected boolean dryrun;

    public void init(String[] args) {
        cfg = ParameterTool.fromArgs(args).getConfiguration();
//        cfg.setString("es_nginx_servers","192.168.80.176:9300,192.168.80.177:9300,192.168.80.178:9300");
//        cfg.setString("kafka.consumer.bootstrap.servers","192.168.10.14:9092,192.168.10.17:9092,192.168.10.18:9092");
//        cfg.setString("kafka.consumer.topic","test-nginx-access-logs");
//        cfg.setString("kafka.consumer.group.id","pepsi-nginx");
//        cfg.setInteger("start_mode",1);
        env = cfg.getBoolean("local", false) ?
                StreamExecutionEnvironment.createLocalEnvironment() :
                StreamExecutionEnvironment.getExecutionEnvironment();
        debug = cfg.getBoolean("debug", false);
        dryrun = cfg.getBoolean("dryrun", false);
        kafkaConsumerCfg = parseConfig("kafka.consumer.");



        ExecutionConfig exeCfg = env.getConfig();
        exeCfg.disableSysoutLogging();
        exeCfg.setRestartStrategy(RestartStrategies.fixedDelayRestart(cfg.getInteger("restart_attempt", 5),
                Time.seconds(cfg.getInteger("restart_delay", 60))));
        exeCfg.setUseSnapshotCompression(cfg.getBoolean("snapshot_compression", false));
        exeCfg.setGlobalJobParameters(cfg);
    }

    protected  void execute() throws Exception {
        preExecute();
        env.execute("StreamingJob");
    }

    /***
     * 不同的job 不同的算子
     */
    protected abstract void preExecute();


    /***
     * kafka datasource
     * @param codec
     * @param <T>
     * @return
     */
    protected <T> FlinkKafkaConsumerBase<T> createKafkaSource(KeyedDeserializationSchema<T> codec) {
        if (kafkaConsumerCfg == null || kafkaConsumerCfg.isEmpty()) {
            throw new IllegalArgumentException("Kafka config is missing.");
        }
        String topic = kafkaConsumerCfg.getProperty("topic");
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("Kafka topic is missing.");
        }

        FlinkKafkaConsumer010<T> kafkaSource = new FlinkKafkaConsumer010<>(topic, codec, kafkaConsumerCfg);

        // 0 表示从topic最早的数据消费，1表示从topic最新的数据消费()
        String startMode = cfg.getString("start_mode","1");
        if ("0".equals(startMode)) {
            kafkaSource.setStartFromEarliest();
        } else if ("1".equals(startMode)) {
            kafkaSource.setStartFromLatest();
        }
        return kafkaSource;
    }

    protected Properties parseConfig(String prefix) {
        Map<String, String> map = cfg.toMap();
        Properties result = new Properties();
        for (Map.Entry<String, String> it : map.entrySet()) {
            String key = it.getKey();
            String val = it.getValue();

            if (key.length() > prefix.length() && key.startsWith(prefix)) {
                result.put(key.substring(prefix.length()), val);
            }
        }
        return result;
    }
}
