package com.rbittencourt.aws.cost.miner.domain.billing;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
public class BillingQuery {

    private static final String WITHOUT_GROUPER = "Without grouper";

    public List<BillingInfo> filter(List<BillingInfo> lineInfos, List<Predicate<BillingInfo>> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return lineInfos;
        }

        return lineInfos.parallelStream()
                .filter(criteria.parallelStream().reduce(x -> true, Predicate::and))
                .collect(Collectors.toList());
    }

    public Map<String, List<BillingInfo>> groupBy(List<BillingInfo> lineInfos, Function<BillingInfo, String> criterion) {
        if (criterion == null) {
            return Map.of(WITHOUT_GROUPER, lineInfos);
        }

        return lineInfos.parallelStream()
                .collect(groupingBy(criterion));
    }

    public BigDecimal totalCost(List<BillingInfo> lineInfos) {
        return lineInfos.parallelStream()
                .map(BillingInfo::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<BillingInfo> betweenTimeRangeOfUsageStartDate(List<BillingInfo> billingInfos, LocalTime start, LocalTime end) {
        Predicate<BillingInfo> equalsStart = b -> b.getUsageStartDate().toLocalTime().equals(start);
        Predicate<BillingInfo> afterStart = b -> b.getUsageStartDate().toLocalTime().isAfter(start);
        Predicate<BillingInfo> beforeEnd = b -> b.getUsageStartDate().toLocalTime().isBefore(end);

        Predicate<BillingInfo> criteria;
        if (start.isAfter(end)) {
            criteria = equalsStart.or(afterStart.or(beforeEnd));
        } else {
            criteria = equalsStart.or(afterStart.and(beforeEnd));
        }

        return billingInfos.stream()
                .filter(b -> b.getUsageStartDate() != null)
                .filter(criteria)
                .collect(toList());
    }

}
