package com.rbittencourt.aws.cost.miner.domain.metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class MetricResult {

    private String metricName;
    private List<MetricValue> metricValues;

    public MetricResult(String metricName, MetricValue metricValue) {
        this.metricName = metricName;
        this.metricValues = asList(metricValue);
    }

    public MetricResult(String metricName, List<MetricValue> metricValues) {
        this.metricName = metricName;
        this.metricValues = new ArrayList<>(metricValues);
    }

    public Optional<String> getMetricName() {
        return Optional.ofNullable(metricName);
    }

    public List<MetricValue> getMetricValues() {
        return metricValues;
    }

}
