package com.pepsi.function;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 17:21
 * Description: No Description
 */
public interface PercentileSummary  extends Serializable {
    void add(double value);

    void merge(PercentileSummary other);

    Double getPercentile(double percentile);
}
