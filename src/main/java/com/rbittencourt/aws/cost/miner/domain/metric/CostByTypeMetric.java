package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.mask.MoneyMaskedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

@Component
@Order(1)
class CostByTypeMetric implements Metric {

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Cost by Usage Type";
    }

    @Override
    public MetricResult calculateMetric(List<BillingInfo> billingInfo) {
        Map<String, List<BillingInfo>> billingByUsageType = billingQuery.groupBy(billingInfo, BillingInfo::getUsageType);

        List<MetricValue> metricValues = billingByUsageType.entrySet().parallelStream()
                .filter(e -> !e.getKey().isEmpty())
                .map(e -> {
                    BigDecimal totalCost = billingQuery.totalCost(e.getValue());
                    return new MetricValue(e.getKey(), totalCost, new MoneyMaskedValue(totalCost));
                })
                .filter(m -> m.getValue().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0)
                .sorted(comparing(MetricValue::getValue, reverseOrder()))
                .collect(toList());

        return new MetricResult(description(), metricValues);
    }

}
