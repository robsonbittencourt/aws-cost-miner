package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.mask.Money;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Order(0)
@Component
@Qualifier("ec2")
class TotalCostMetric implements Metric {

    @Override
    public String description() {
        return null;
    }

    public MetricResult calculateMetric(BillingInfos billingInfos) {
        BigDecimal totalCost = billingInfos.totalCost();

        MetricValue metricValue = new MetricValue("Total cost", totalCost, new Money(totalCost));

        return new MetricResult(description(), metricValue);
    }

}
