package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.mask.Money;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.TemporalQuery;
import java.util.List;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_EVEN;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;

@Order(4)
@Component
@Qualifier("ec2")
class CostMeanInWeekendDays implements Metric {

    private static final TemporalQuery<Boolean> IS_WEEKEND_QUERY = t -> t.get(DAY_OF_WEEK) > 5;

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(BillingInfos billingInfos) {
        Predicate<BillingInfo> hasUsageStartDate = b -> b.getUsageStartDate() != null;
        Predicate<BillingInfo> isWeekend = b -> b.getUsageStartDate().query(IS_WEEKEND_QUERY);

        BillingInfos weekendBillings = billingInfos.filter(hasUsageStartDate, isWeekend);

        long weekendQuantity = weekendBillings.stream()
                .map(b -> b.getUsageStartDate().toLocalDate())
                .distinct()
                .count();

        BigDecimal totalCost = weekendBillings.totalCost();
        BigDecimal average = weekendQuantity != 0 ? totalCost.divide(new BigDecimal(weekendQuantity), HALF_EVEN) : totalCost;

        MetricValue metricValue = new MetricValue("Cost mean in weekend days", average, new Money(average));

        return new MetricResult(description(), List.of(metricValue));
    }
}
