package com.pepsi.nglog.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-06-29 23:08
 * Description: No Description
 */
public class NginxLogStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private long timestamp;
    private String domain;
    private String urlPath;
    private int status;
    private long durationP99;
    private double errorRate;
    private long count;
    private double qps;
    private String key;
    private String upstreamAddr;
    private long durationSum;
    private long durationMax;
    private long durationAvg;
    private long errorCount;
    private long timeWindow;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDurationP99() {
        return durationP99;
    }

    public void setDurationP99(long durationP99) {
        this.durationP99 = durationP99;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getQps() {
        return qps;
    }

    public void setQps(double qps) {
        this.qps = qps;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUpstreamAddr() {
        return upstreamAddr;
    }

    public void setUpstreamAddr(String upstreamAddr) {
        this.upstreamAddr = upstreamAddr;
    }

    public long getDurationSum() {
        return durationSum;
    }

    public void setDurationSum(long durationSum) {
        this.durationSum = durationSum;
    }

    public long getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(long durationMax) {
        this.durationMax = durationMax;
    }

    public long getDurationAvg() {
        return durationAvg;
    }

    public void setDurationAvg(long durationAvg) {
        this.durationAvg = durationAvg;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }

    public long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(long timeWindow) {
        this.timeWindow = timeWindow;
    }
}
