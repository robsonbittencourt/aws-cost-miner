package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.mask.MoneyMaskedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static java.math.RoundingMode.HALF_EVEN;

@Order(2)
@Component
class CostMeanByDay implements Metric {

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(List<BillingInfo> billingInfos) {
        long daysQuantity = billingInfos.stream()
                .filter(b -> b.getUsageStartDate() != null)
                .map(b -> b.getUsageStartDate().toLocalDate())
                .distinct()
                .count();

        BigDecimal totalCost = billingQuery.totalCost(billingInfos);
        BigDecimal average = totalCost.divide(new BigDecimal(daysQuantity), HALF_EVEN);

        MetricValue metricValue = new MetricValue("Cost mean by day", average, new MoneyMaskedValue(average));

        return new MetricResult(description(), List.of(metricValue));
    }

}
