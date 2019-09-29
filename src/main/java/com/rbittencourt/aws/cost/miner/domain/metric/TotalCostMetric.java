package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.mask.MoneyMaskedValue;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(0)
class TotalCostMetric implements Metric {

    @Override
    public String description() {
        return null;
    }

    public MetricResult calculateMetric(BillingInfos billingInfos) {
        BigDecimal totalCost = billingInfos.totalCost();

        MetricValue metricValue = new MetricValue("Total cost", totalCost, new MoneyMaskedValue(totalCost));

        return new MetricResult(description(), metricValue);
    }

}
