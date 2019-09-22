package com.rbittencourt.aws.cost.miner.domain.metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class MetricResult {

    private String description;
    private List<MetricValue> metricValues;

    public MetricResult(String description, MetricValue metricValue) {
        this.description = description;
        this.metricValues = asList(metricValue);
    }

    public MetricResult(String description, List<MetricValue> metricValues) {
        this.description = description;
        this.metricValues = new ArrayList<>(metricValues);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public List<MetricValue> getMetricValues() {
        return metricValues;
    }

}
