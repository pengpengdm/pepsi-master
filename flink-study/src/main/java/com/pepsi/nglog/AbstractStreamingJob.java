package com.pepsi.nglog;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.Serializable;
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
        env = cfg.getBoolean("local", true) ?
                StreamExecutionEnvironment.createLocalEnvironment() :
                StreamExecutionEnvironment.getExecutionEnvironment();
        debug = cfg.getBoolean("debug", false);
        dryrun = cfg.getBoolean("dryrun", false);
        cfg.setString("es_nginx_servers","192.168.80.176:9300,192.168.80.177:9300,192.168.80.178:9300");
    }

    protected  void execute() throws Exception {
        preExecute();
        env.execute("StreamingJob");
    }

    /***
     * 不同的job 不同的算子
     */
    protected abstract void preExecute();
}
