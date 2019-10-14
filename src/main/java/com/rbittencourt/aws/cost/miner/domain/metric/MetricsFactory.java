package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.application.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rbittencourt.aws.cost.miner.application.report.ReportType.EC2_COST_REPORT;
import static com.rbittencourt.aws.cost.miner.application.report.ReportType.RESERVED_INSTANCE_USAGE_REPORT;

@Component
public class MetricsFactory {

    @Autowired
    @Qualifier("ec2")
    private List<Metric> ec2Metrics;

    @Autowired
    @Qualifier("reservedInstance")
    private List<Metric> reservedInstanceMetrics;

    public List<Metric> build(Report report) {
        if (report != null && report.reportType().equals(EC2_COST_REPORT)) {
            return ec2Metrics;
        }

        if (report != null && report.reportType().equals(RESERVED_INSTANCE_USAGE_REPORT)) {
            return reservedInstanceMetrics;
        }

        throw new IllegalArgumentException("Invalid Report");
    }

}
