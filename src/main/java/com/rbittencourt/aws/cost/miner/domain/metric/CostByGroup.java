package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.billing.EC2PricingModel;
import com.rbittencourt.aws.cost.miner.domain.mask.Hour;
import com.rbittencourt.aws.cost.miner.domain.mask.Money;
import com.rbittencourt.aws.cost.miner.domain.mask.Percent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.rbittencourt.aws.cost.miner.domain.billing.EC2PricingModel.*;
import static com.rbittencourt.aws.cost.miner.domain.utils.BigDecimalUtils.percentOf;
import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toMap;

@Component
class CostByGroup {

    public List<MetricValue> metricValues(BillingInfos billingInfos, Function<BillingInfo, String> criterion, String groupDescription) {
        BillingInfos instancesBilling = billingInfos.filter(BillingInfo::isEC2Instance);

        Map<String, BillingInfos> billingByInstanceFamily = sort(instancesBilling.groupBy(criterion));

        List<MetricValue> values = new ArrayList<>();

        for (Map.Entry<String, BillingInfos> entry : billingByInstanceFamily.entrySet()) {
            String instanceType = entry.getKey();
            BigDecimal totalHours = entry.getValue().totalHoursUsed();
            BigDecimal totalCost = entry.getValue().totalCost();
            Map<String, BillingInfos> byPricingModel = entry.getValue().groupBy(b -> b.ec2PricingModel().getDescription());

            values.addAll(metricOfType(instanceType, ON_DEMAND, totalHours, totalCost, byPricingModel, groupDescription));
            values.addAll(metricOfType(instanceType, SPOT_INSTANCE, totalHours, totalCost, byPricingModel, groupDescription));
            values.addAll(metricOfType(instanceType, RESERVED_INSTANCE, totalHours, totalCost, byPricingModel, groupDescription));
            values.addAll(metricOfType(instanceType, SAVINGS_PLAN, totalHours, totalCost, byPricingModel, groupDescription));
            values.addAll(total(entry, groupDescription));
        }

        return values;
    }

    private Map<String, BillingInfos> sort(Map<String, BillingInfos> billingInfos) {
        return billingInfos.entrySet().parallelStream()
                .sorted(comparing(v -> v.getValue().totalCost(), reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private List<MetricValue> metricOfType(String instanceType, EC2PricingModel pricingModel, BigDecimal totalHours, BigDecimal totalCost, Map<String, BillingInfos> byPricingModel, String groupDescription) {
        List<MetricValue> values = new ArrayList<>();

        if (groupDescription != null) {
            values.add(new MetricValue(groupDescription, instanceType));
        }

        values.add(new MetricValue("Pricing Model", pricingModel.getDescription()));

        BillingInfos billingInfos = byPricingModel.get(pricingModel.getDescription());
        values.addAll(hoursUsed(totalHours, billingInfos));
        values.addAll(cost(totalCost, billingInfos));

        return values;
    }

    private List<MetricValue> hoursUsed(BigDecimal totalHours, BillingInfos billingInfos) {
        BigDecimal totalHoursByPricingModel = ZERO;
        BigDecimal totalHoursPercent = ZERO;

        if (billingInfos != null) {
            totalHoursByPricingModel = billingInfos.totalHoursUsed();
            totalHoursPercent = percentOf(totalHoursByPricingModel, totalHours);
        }

        MetricValue hours = new MetricValue("Hours", new Hour(totalHoursByPricingModel).getMaskedValue());
        MetricValue hoursPercent = new MetricValue("Hours %", new Percent(totalHoursPercent).getMaskedValue());

        return List.of(hours, hoursPercent);
    }

    private List<MetricValue> cost(BigDecimal totalCost, BillingInfos billingInfos) {
        BigDecimal totalCostByPricingModel = ZERO;
        BigDecimal totalCostPercent = ZERO;

        if (billingInfos != null) {
            totalCostByPricingModel = billingInfos.totalCost();
            totalCostPercent = percentOf(totalCostByPricingModel, totalCost);
        }

        MetricValue cost = new MetricValue("Cost", new Money(totalCostByPricingModel).getMaskedValue());
        MetricValue costPercent = new MetricValue("Cost %", new Percent(totalCostPercent).getMaskedValue());

        return List.of(cost, costPercent);
    }

    private List<MetricValue> total(Map.Entry<String, BillingInfos> entry, String groupDescription) {
        List<MetricValue> values = new ArrayList<>();

        if (groupDescription != null) {
            values.add(new MetricValue(groupDescription, entry.getKey()));
        }

        values.add(new MetricValue("Pricing Model", "Total"));
        values.add(new MetricValue("Hours", new Hour(entry.getValue().totalHoursUsed()).getMaskedValue()));
        values.add(new MetricValue("Hours %", new Percent(new BigDecimal(100)).getMaskedValue()));
        values.add(new MetricValue("Cost", new Money(entry.getValue().totalCost()).getMaskedValue()));
        values.add(new MetricValue("Cost %", new Percent(new BigDecimal(100)).getMaskedValue()));

        return values;
    }

}
