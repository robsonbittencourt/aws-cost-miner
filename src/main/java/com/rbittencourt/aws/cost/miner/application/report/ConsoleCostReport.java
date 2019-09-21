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
        AwsProduct awsProduct = product != null ? AwsProduct.valueOf(product) : null;

        SearchParameters searchParameters = new SearchParameters();

        if (awsProduct != null) {
            searchParameters.addFilter(b -> awsProduct.getName().equals(b.getProductName()));
        }

        searchParameters.addFilter(b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0);

        if (groupBy != null) {
            searchParameters.addGrouper(buildGroupByClause());
        }

        List<MinedData> minedData = miner.miningCostData(awsProduct, searchParameters);

        if (awsProduct != null) {
            System.out.println("========== Cost Report - " + awsProduct.name() + " ==========" + "\n");
        } else {
            System.out.println("========== Cost Report" + " ==========" + "\n");
        }

        for (MinedData data : minedData) {
            String target = isEmpty(data.getTarget()) ? "Without grouper" : data.getTarget();

            System.out.println("-----------------------------------------------");
            System.out.println("| " + target + " |") ;
            System.out.println("-----------------------------------------------");

            for (MetricResult metricResult : data.getMetricResult()) {
                boolean indent = false;
                if(metricResult.getDescription().isPresent()) {
                    System.out.println(metricResult.getDescription().get());
                    indent = true;
                }

                for (MetricValue metricValue : metricResult.getMetricValues()) {
                    if (indent) {
                        System.out.print("\t");
                    }
                    System.out.println(metricValue);
                }

                System.out.println("");
            }

            System.out.println("");
        }
    }

    private Function<BillingInfo, String> buildGroupByClause() {
        return b -> {
            try {
                Method method = b.getClass().getMethod("get" + groupBy);
                return method.invoke(b).toString();
            } catch (Exception e) {
                System.out.println("");
            }

            return b.getCustomField(groupBy);
        };
    }

}
