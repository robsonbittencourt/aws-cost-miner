package com.rbittencourt.aws.cost.miner.application.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportFactory {

    @Autowired
    private EC2CostReport ec2CostReport;

    @Autowired
    private ReservedInstanceUsageReport reservedInstanceUsageReport;

    public Report getInstance(ReportType reportType) {
        switch (reportType) {
            case EC2_COST_REPORT:
                return ec2CostReport;

            case RESERVED_INSTANCE_USAGE_REPORT:
                return reservedInstanceUsageReport;

            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }

}
