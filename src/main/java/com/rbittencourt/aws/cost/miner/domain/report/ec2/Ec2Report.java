package com.rbittencourt.aws.cost.miner.domain.report.ec2;

import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Ec2Report implements Report {

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
