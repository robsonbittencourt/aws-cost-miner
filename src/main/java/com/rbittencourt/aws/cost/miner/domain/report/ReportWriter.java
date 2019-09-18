package com.rbittencourt.aws.cost.miner.domain.report;

import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ReportWriter {

    public void write(Report report) {
        System.out.println("========== " + report.description() + " ==========" + "\n");

        for (Metric metric : report.metrics()) {
            if (!isEmpty(metric.description())) {
                System.out.println(metric.description());
            }

            String line = metric.calculateMetric(report.billingLines());
            System.out.println(line);
        }
    }
}
