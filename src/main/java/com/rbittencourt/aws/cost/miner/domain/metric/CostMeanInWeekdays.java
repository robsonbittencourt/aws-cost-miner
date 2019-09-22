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

@Order(3)
@Component
class CostMeanInWeekdays implements Metric {

    private static final TemporalQuery<Boolean> IS_WEEKDAY_QUERY = t -> t.get(DAY_OF_WEEK) < 6;

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Daily Metrics";
    }

    @Override
    public MetricResult calculateMetric(List<BillingInfo> billingInfos) {
        List<BillingInfo> weekdayBillings = billingInfos.stream().filter(b -> b.getUsageStartDate().query(IS_WEEKDAY_QUERY)).collect(toList());
        long weekdayDaysQuantity = weekdayBillings.stream().map(b -> b.getUsageStartDate().toLocalDate()).distinct().count();

        BigDecimal totalCost = billingQuery.totalCost(weekdayBillings);
        BigDecimal average = weekdayDaysQuantity != 0 ? totalCost.divide(new BigDecimal(weekdayDaysQuantity), HALF_EVEN) : totalCost;

        MetricValue metricValue = new MetricValue("Cost mean in weekdays", average, new MoneyMaskedValue(average));

        return new MetricResult(description(), List.of(metricValue));
    }

}
