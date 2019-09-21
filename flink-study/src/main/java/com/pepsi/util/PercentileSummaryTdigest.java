package com.pepsi.util;

import com.pepsi.function.PercentileSummary;
import com.tdunning.math.stats.TDigest;

/**
 * Created with IntelliJ IDEA.
 * User: pepsi
 * Date: 2019-09-20 17:22
 * Description: No Description
 */
public class PercentileSummaryTdigest implements PercentileSummary {

    private TDigest digest;

    public PercentileSummaryTdigest(double compression) {
        this.digest = TDigest.createMergingDigest(compression);
    }

    @Override
    public void add(double value) {
        digest.add(value);
    }

    @Override
    public void merge(PercentileSummary other) {
        if(other instanceof PercentileSummaryTdigest){
            digest.add(((PercentileSummaryTdigest) other).digest);
        }
    }

    @Override
    public Double getPercentile(double percentile) {
        return digest.quantile(percentile);
    }
}
