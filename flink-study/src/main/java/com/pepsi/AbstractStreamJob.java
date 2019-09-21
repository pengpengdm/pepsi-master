package com.pepsi;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 10:47
 * Description: No Description
 */
public abstract class AbstractStreamJob implements Serializable {


    protected Configuration config;

    protected StreamExecutionEnvironment env;

    protected Properties kafkaConsumerCfg;

    protected void init(String[] args){
        config = ParameterTool.fromArgs(args).getConfiguration();
        env = config.getBoolean("local",false) ?
                StreamExecutionEnvironment.getExecutionEnvironment() :
                StreamExecutionEnvironment.createLocalEnvironment();

        long interval = config.getLong("checkpoint_interval",30000);
        if(interval > 0){
            //todo 添加 checkpoint 相关
        }

        kafkaConsumerCfg = parseConfig("kafka.consumer.");
        //添加到flink配置中
       /* ExecutionConfig exeCfg = env.getConfig();
        exeCfg.disableSysoutLogging();
        exeCfg.setRestartStrategy(RestartStrategies.fixedDelayRestart(config.getInteger("restart_attempt", 5),
                Time.seconds(config.getInteger("restart_delay", 60))));
        exeCfg.setUseSnapshotCompression(config.getBoolean("snapshot_compression", false));
        exeCfg.setGlobalJobParameters(config);*/
        //特殊 job 的处理
        postInit();
    }

    protected abstract void postInit();


    protected void execute() throws Exception {
        preExecute();
        env.execute(config.getString("job.name",this.getClass().getName()));
    }

    protected abstract void preExecute();


    /***
     * 传参数的解析
     * @param prefix
     * @return
     */
    protected Properties parseConfig(String prefix){
        Map<String, String> map = config.toMap();
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


    /***
     * 加载文件 Properties
     * @param cfgName
     * @param maxSize
     * @return
     */
    protected Properties loadConfig(String cfgName,long maxSize) throws IOException {
        String path = config.getString(cfgName, null);
        if (path == null) {
            throw new IllegalArgumentException(cfgName + " is missing");
        }
        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            throw new IllegalArgumentException("Invalid file " + path);
        }
        long len  = f.length();
        if(len>maxSize){
            throw new IllegalArgumentException(path + " is too large and is over " + maxSize);
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(f));
        return properties;
    }


    /***
     *
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
        FlinkKafkaConsumer010<T> kafkaSource = new FlinkKafkaConsumer010<T>(topic,codec,kafkaConsumerCfg);
        String start_mode = kafkaConsumerCfg.getProperty("start_mode");
        if("0".equals(start_mode)){
            kafkaSource.setStartFromEarliest();
        }else if("1".equals(start_mode)){
            kafkaSource.setStartFromLatest();
        }
        return kafkaSource;
    }
}
