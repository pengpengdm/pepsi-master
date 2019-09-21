package com.pepsi.bean;

import com.pepsi.function.PercentileSummary;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 17:03
 * Description: No Description
 */
public class NginxLogStats implements Serializable {

    private String appName;
    /***
     * 统计窗口的开始时间(ms)
     */
    private long timestamp;

    /***
     * 统计窗口的长度(ms)
     */
    private long timeWindow;

    /***
     * 域名
     */
    private String domain;

    /**
     * 请求路径
     */
    private String path;


    /**
     * Nginx 上游IP (e.g. [ip]:[port], [ip])
     */
    private String upstreamAddr;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * 调用时间总和(ms)
     */
    private long durationSum;

    /**
     * 调用时间平均值(ms)
     */
    private long durationAvg;

    /**
     * 调用时间最大值(ms)
     */
    private long durationMax;

    /***
     * 耗时99线
     */
    private long durationP99;

    /**
     * 调用次数
     */
    private long count;

    /**
     * 错误调用次数
     */
    private int errorStatusCount;

    /**
     * slowurl 次数
     */
    private int slowUrlCount;


    /**
     * Percentile summary
     */
    private PercentileSummary summary;

    /**
     * status错误率
     */
    private double errorStatusRate;

    /**
     * 慢 url 发生率
     */
    private double slowUrlRate;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(long timeWindow) {
        this.timeWindow = timeWindow;
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

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpstreamAddr() {
        return upstreamAddr;
    }

    public void setUpstreamAddr(String upstreamAddr) {
        this.upstreamAddr = upstreamAddr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDurationSum() {
        return durationSum;
    }

    public void setDurationSum(long durationSum) {
        this.durationSum = durationSum;
    }

    public long getDurationAvg() {
        return durationAvg;
    }

    public void setDurationAvg(long durationAvg) {
        this.durationAvg = durationAvg;
    }

    public long getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(long durationMax) {
        this.durationMax = durationMax;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getErrorStatusCount() {
        return errorStatusCount;
    }

    public void setErrorStatusCount(int errorStatusCount) {
        this.errorStatusCount = errorStatusCount;
    }

    public int getSlowUrlCount() {
        return slowUrlCount;
    }

    public void setSlowUrlCount(int slowUrlCount) {
        this.slowUrlCount = slowUrlCount;
    }

    public PercentileSummary getSummary() {
        return summary;
    }

    public void setSummary(PercentileSummary summary) {
        this.summary = summary;
    }

    public double getErrorStatusRate() {
        return errorStatusRate;
    }

    public void setErrorStatusRate(double errorStatusRate) {
        this.errorStatusRate = errorStatusRate;
    }

    public double getSlowUrlRate() {
        return slowUrlRate;
    }

    public void setSlowUrlRate(double slowUrlRate) {
        this.slowUrlRate = slowUrlRate;
    }

    public long getDurationP99() {
        return durationP99;
    }

    public void setDurationP99(long durationP99) {
        this.durationP99 = durationP99;
    }
}
