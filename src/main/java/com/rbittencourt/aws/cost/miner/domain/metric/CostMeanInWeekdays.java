package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.mask.MoneyMaskedValue;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.TemporalQuery;
import java.util.List;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_EVEN;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;

@Order(3)
@Component
class CostMeanInWeekdays implements Metric {

    private static final TemporalQuery<Boolean> IS_WEEKDAY_QUERY = t -> t.get(DAY_OF_WEEK) < 6;

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(BillingInfos billingInfos) {
        Predicate<BillingInfo> hasUsageStartDate = b -> b.getUsageStartDate() != null;
        Predicate<BillingInfo> isWeekend = b -> b.getUsageStartDate().query(IS_WEEKDAY_QUERY);

        BillingInfos weekdayBillings = billingInfos.filter(hasUsageStartDate, isWeekend);

        long weekdayDaysQuantity = weekdayBillings.stream()
                .map(b -> b.getUsageStartDate().toLocalDate())
                .distinct()
                .count();

        BigDecimal totalCost = weekdayBillings.totalCost();
        BigDecimal average = weekdayDaysQuantity != 0 ? totalCost.divide(new BigDecimal(weekdayDaysQuantity), HALF_EVEN) : totalCost;

        MetricValue metricValue = new MetricValue("Cost mean in weekdays", average, new MoneyMaskedValue(average));

        return new MetricResult(description(), List.of(metricValue));
    }

}
