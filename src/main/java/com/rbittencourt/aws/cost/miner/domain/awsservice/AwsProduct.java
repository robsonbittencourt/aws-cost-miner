package com.rbittencourt.aws.cost.miner.domain.awsservice;

import com.rbittencourt.aws.cost.miner.domain.metric.Metric;

import java.util.List;

public interface AwsProduct {

    String description();

    String awsProduct();

    List<Metric> metrics();

}
