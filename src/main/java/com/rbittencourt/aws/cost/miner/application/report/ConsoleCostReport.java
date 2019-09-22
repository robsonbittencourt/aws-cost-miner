package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricValue;
import com.rbittencourt.aws.cost.miner.domain.miner.AwsCostMiner;
import com.rbittencourt.aws.cost.miner.domain.miner.MinedData;
import com.rbittencourt.aws.cost.miner.domain.miner.SearchParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import static java.math.RoundingMode.HALF_EVEN;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ConsoleCostReport {

    @Autowired
    private AwsCostMiner miner;

    @Value("${product:#{null}}")
    private String product;

    @Value("${groupBy:#{null}}")
    private String groupBy;

    public void writeReport() {
        AwsProduct awsProduct = AwsProduct.fromName(product);
        SearchParameters searchParameters = buildSearchParameters(awsProduct);
        List<MinedData> minedData = miner.miningCostData(awsProduct, searchParameters);

        printTitle(awsProduct);

        for (MinedData data : minedData) {
            BigDecimal totalCost = data.getMetricResult().get(0).getMetricValues().get(0).getValue().setScale(2, HALF_EVEN);

            if (totalCost.compareTo(new BigDecimal("0.00")) > 0) {
                printHeader(data);

                printMetricResult(data);

                blankLine("");
            }
        }
    }

    private void printTitle(AwsProduct awsProduct) {
        if (awsProduct != null) {
            System.out.println("========== Cost Report - " + awsProduct.name() + " ==========" + "\n");
        } else {
            System.out.println("========== Cost Report" + " ==========" + "\n");
        }
    }

    private void printHeader(MinedData data) {
        String target = isEmpty(data.getTarget()) ? "Without grouper" : data.getTarget();
        System.out.println("-----------------------------------------------");
        System.out.println("| " + target + " |") ;
        System.out.println("-----------------------------------------------");
    }

    private void printMetricResult(MinedData data) {
        for (MetricResult metricResult : data.getMetricResult()) {
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

            blankLine("");
        }
    }

    private void blankLine(String s) {
        System.out.println(s);
    }

    private SearchParameters buildSearchParameters(AwsProduct awsProduct) {
        SearchParameters searchParameters = new SearchParameters();

        if (awsProduct != null) {
            searchParameters.addFilter(b -> awsProduct.getDescription().equals(b.getProductName()));
        }

        if (groupBy != null) {
            searchParameters.addGrouper(buildGroupByClause());
        }
        return searchParameters;
    }

    private Function<BillingInfo, String> buildGroupByClause() {
        return b -> {
            try {
                Method method = b.getClass().getMethod("get" + groupBy);
                return method.invoke(b).toString();
            } catch (Exception e) {
                // do nothing. This is expected.
            }

            return b.getCustomField(groupBy);
        };
    }

}
