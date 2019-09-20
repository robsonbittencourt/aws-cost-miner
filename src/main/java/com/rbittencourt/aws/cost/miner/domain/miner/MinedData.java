package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;

import java.util.List;

public class MinedData {

    private String target;
    private List<MetricResult> metricResult;

    public MinedData(String target, List<MetricResult> metricResult) {
        this.target = target;
        this.metricResult = metricResult;
    }

    public String getTarget() {
        return target;
    }

    public List<MetricResult> getMetricResult() {
        return metricResult;
    }

}
