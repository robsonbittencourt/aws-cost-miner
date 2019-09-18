package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
@Order(1)
public class CostByTypeMetric implements Metric {

    private static final DecimalFormat FORMATTER = new DecimalFormat("0.0000000000");

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Cost by Usage Type";
    }

    @Override
    public String calculateMetric(List<BillingInfo> billingInfo) {
        Map<String, List<BillingInfo>> billingByUsageType = billingQuery.groupBy(billingInfo, BillingInfo::getUsageType);

        Map<String, BigDecimal> collect = billingByUsageType.entrySet().stream()
                .collect(toMap(
                        Map.Entry::getKey,
                        e -> billingQuery.totalCost(e.getValue())
                ));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, BigDecimal> entry : collect.entrySet()) {
            sb.append("\t").append(entry.getKey()).append(": ").append(FORMATTER.format(entry.getValue())).append("\n");
        }

        return sb.toString();
    }

}
