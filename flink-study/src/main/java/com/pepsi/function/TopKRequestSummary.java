package com.pepsi.function;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-23 23:57
 * Description: No Description
 */
public class TopKRequestSummary<T> extends TopKSummary<String, T>{
    private String type;
    private long timestamp;
    private long timeWindow;

    public TopKRequestSummary(int capacity) {
        super(capacity);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String logToString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("type = ").append(type).append("; timestamp = ").append(timestamp).append("; timeWindow = ").append(timeWindow).append("\n");

        List<Stats<String, T>> all = getAll(true);
        if (all != null && !all.isEmpty()) {
            for (Stats<String, T> stats : all) {
                sb.append("\t").append(stats).append("\n");
            }
        }
        return sb.toString();
    }
}
