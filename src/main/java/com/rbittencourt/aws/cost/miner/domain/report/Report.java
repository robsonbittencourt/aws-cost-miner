package com.rbittencourt.aws.cost.miner.domain.report;

import com.rbittencourt.aws.cost.miner.domain.metric.Metric;

import java.util.List;

public interface Report {

    String description();

    String awsProduct();

    List<Metric> metrics();

}
