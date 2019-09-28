package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.mask.MoneyMaskedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.TemporalQuery;
import java.util.List;

import static java.math.RoundingMode.HALF_EVEN;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.util.stream.Collectors.toList;

@Order(4)
@Component
class CostMeanInWeekendDays implements Metric {

    private static final TemporalQuery<Boolean> IS_WEEKEND_QUERY = t -> t.get(DAY_OF_WEEK) > 5;

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(List<BillingInfo> billingInfos) {
        List<BillingInfo> weekendBillings = billingInfos.stream()
                .filter(b -> b.getUsageStartDate() != null)
                .filter(b -> b.getUsageStartDate().query(IS_WEEKEND_QUERY))
                .collect(toList());

        long weekendQuantity = weekendBillings.stream()
                .map(b -> b.getUsageStartDate().toLocalDate())
                .distinct()
                .count();

        BigDecimal totalCost = billingQuery.totalCost(weekendBillings);
        BigDecimal average = weekendQuantity != 0 ? totalCost.divide(new BigDecimal(weekendQuantity), HALF_EVEN) : totalCost;

        MetricValue metricValue = new MetricValue("Cost mean in weekend days", average, new MoneyMaskedValue(average));

        return new MetricResult(description(), List.of(metricValue));
    }
}
