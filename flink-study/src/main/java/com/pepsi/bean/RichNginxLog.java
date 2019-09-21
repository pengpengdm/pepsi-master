package com.pepsi.bean;

import com.pepsi.util.FlinkUtils;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.flink.util.StringUtils;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 14:37
 * Description: No Description
 */
public class RichNginxLog implements Serializable {

    private static final long serialVersionUID = 1L;

    // 时间
    private long timestamp;
    //域名
    private String domain;
    //请求路径
    private String path;
    //状态
    private int status;
    //qingq响应时间
    private long duration;
    // ip
    private String upstream;
    //应用名称
    private String appName;

    public long getTimestamp() {
        return timestamp;
    }

    @JsonSetter(value = "@timestamp")
    public void setTimestamp(String value) {
        if(!StringUtils.isNullOrWhitespaceOnly(value)){
            int length = value.length();
            switch (length) {
                case 25: // e.g. 2019-06-18T17:25:00+08:00
                    this.timestamp = FlinkUtils.parseIsoTimestamp(value);
                    break;
                case 24: // e.g. 2019-06-18T09:25:02.307Z
                    this.timestamp = FlinkUtils.parseFilebeatTimestamp(value);
                    break;
                default:
                    try {
                        // 默认为 ms, 用于测试数据
                        this.timestamp = Long.parseLong(value, 10);
                    } catch (Exception ignored) {
                    }
                    break;
            }
        }
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    @JsonSetter(value = "request")
    public void setPath(String value) {
        if(!StringUtils.isNullOrWhitespaceOnly(value)){
            int index = value.indexOf("?");
            if(index > 0){
                this.path = value.substring(0,index);
                return;
            }else if(index == 0){
                this.path="/";
                return;
            }
        }else
            value="";
        this.path = value;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    @JsonSetter(value = "responsetime")
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUpstream() {
        return upstream;
    }

    @JsonSetter(value = "upstreamaddr")
    public void setUpstream(String value) {
        this.upstream = value;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
