package com.rbittencourt.aws.cost.miner.domain.metric;

import java.util.List;
import java.util.Optional;

public class MetricResult {

    private Optional<String> description;
    private List<MetricValue> metricValues;

    public MetricResult(String description, MetricValue metricValue) {
        this.description = Optional.ofNullable(description);
        this.metricValues = List.of(metricValue);
    }

    public MetricResult(String description, List<MetricValue> metricValues) {
        this.description = Optional.of(description);
        this.metricValues = metricValues;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public List<MetricValue> getMetricValues() {
        return metricValues;
    }

}
