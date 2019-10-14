package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.mask.Money;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_EVEN;

@Order(2)
@Component
@Qualifier("ec2")
class CostMeanByDay implements Metric {

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(BillingInfos billingInfos) {
        Predicate<BillingInfo> hasUsageStartDate = b -> b.getUsageStartDate() != null;

        long daysQuantity = billingInfos.stream()
                .filter(hasUsageStartDate)
                .map(b -> b.getUsageStartDate().toLocalDate())
                .distinct()
                .count();

        BigDecimal totalCost = billingInfos.filter(hasUsageStartDate).totalCost();
        BigDecimal average = totalCost.divide(new BigDecimal(daysQuantity), HALF_EVEN);

        MetricValue metricValue = new MetricValue("Cost mean by day", average, new Money(average));

        return new MetricResult(description(), List.of(metricValue));
    }

}
