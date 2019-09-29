package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricValue;
import com.rbittencourt.aws.cost.miner.domain.miner.MinedData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_EVEN;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ConsoleCostReportPrinter {

    private static final int REPORT_WIDTH = 60;
    private static final String BLANK_SPACE = " ";

    public void printReport(List<MinedData> minedData) {
        printTitle();

        for (MinedData data : minedData) {
            BigDecimal totalCost = data.getMetricResult().get(0).getMetricValues().get(0).getValue().setScale(2, HALF_EVEN);

            if (totalCost.compareTo(new BigDecimal("0.00")) > 0) {
                printHeader(data);

                printMetricResult(data);

                blankLine();
            }
        }
    }

    private void printTitle() {
        System.out.println("=".repeat(24) + " COST REPORT " + "=".repeat(23));
        blankLine();
    }

    private void printHeader(MinedData data) {
        String target = isEmpty(data.getTarget()) ? "Without grouper" : data.getTarget();
        String tracedLine = "-".repeat(REPORT_WIDTH);

        System.out.println(tracedLine);
        int leftSpace = (REPORT_WIDTH - target.length()) / 2;
        int rightSpace = REPORT_WIDTH - target.length() - leftSpace - 2;

        System.out.println("|" + BLANK_SPACE.repeat(leftSpace) +  target + BLANK_SPACE.repeat(rightSpace) + "|") ;
        System.out.println(tracedLine);
    }

    private void printMetricResult(MinedData data) {
        for (MetricResult metricResult : data.getMetricResult()) {
            if (isEmpty(metricResult.getMetricValues())) {
                continue;
            }

            boolean indent = false;
            if (metricResult.getDescription().isPresent()) {
                System.out.println(metricResult.getDescription().get());
                indent = true;
            }

            for (MetricValue metricValue : metricResult.getMetricValues()) {
                if (indent) {
                    System.out.print("\t");
                }
                System.out.println(metricValue);
            }

            blankLine();
        }
    }

    private void blankLine() {
        System.out.println(BLANK_SPACE);
    }

}
