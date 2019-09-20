package com.rbittencourt.aws.cost.miner.domain.awsservice.ec2;

import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsProduct;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EC2 implements AwsProduct {

    @Autowired
    private List<Metric> metrics;

    @Override
    public String description() {
        return "Cost Report - Amazon Elastic Compute Cloud (EC2)";
    }

    @Override
    public String awsProduct() {
        return "Amazon Elastic Compute Cloud";
    }

    @Override
    public List<Metric> metrics() {
        return metrics;
    }

}
