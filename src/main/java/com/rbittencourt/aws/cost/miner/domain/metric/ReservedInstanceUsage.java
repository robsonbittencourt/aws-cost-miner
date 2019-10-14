package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfo;
import com.rbittencourt.aws.cost.miner.domain.mask.Percent;
import com.rbittencourt.aws.cost.miner.domain.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Component
@Qualifier("reservedInstance")
public class ReservedInstanceUsage implements Metric {

    @Override
    public String description() {
        return "Reserved Instance Usage";
    }

    @Override
    public MetricResult calculateMetric(BillingInfos billingInfos) {
        List<MetricValue> metricValues = new ArrayList<>();

        List<ReservedInstanceInfo> reservedInstanceInfos = billingInfos.reservedInstanceInfos();

        for (ReservedInstanceInfo reservedInfo : reservedInstanceInfos) {
            BigDecimal usageFromReserved = BigDecimal.ZERO;

            BillingInfos infosOfReserved = billingInfos.filter(b -> b.getSubscriptionId().equals(reservedInfo.getSubscriptionId()));

            for (BillingInfo billingInfo : infosOfReserved.getBillingInfos()) {
                BigDecimal normalizedFactor = billingInfo.instanceSize().normalize(reservedInfo.instanceSize());
                usageFromReserved = usageFromReserved.add(billingInfo.getUsedHours().multiply(normalizedFactor));
            }

            BigDecimal percentUsedOfReserved = BigDecimalUtils.percentOf(usageFromReserved, reservedInfo.getUsageQuantity());

            metricValues.add(new MetricValue(reservedInfo.getItemDescription(), percentUsedOfReserved, new Percent(percentUsedOfReserved)));
        }

        return new MetricResult(description(), sort(metricValues));
    }

    private List<MetricValue> sort(List<MetricValue> metricValues) {
        return metricValues.parallelStream()
                .sorted(comparing(MetricValue::getValue))
                .collect(toList());
    }

}
