package com.rbittencourt.aws.cost.miner.domain.billing;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class BillingQuery {

    public final List<BillingInfo> filter(List<BillingInfo> lineInfos, List<Predicate<BillingInfo>> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return lineInfos;
        }

        return lineInfos.parallelStream()
                .filter(criteria.parallelStream().reduce(x -> true, Predicate::and))
                .collect(Collectors.toList());
    }

    public Map<String, List<BillingInfo>> groupBy(List<BillingInfo> lineInfos, Function<BillingInfo, String> criterion) {
        if (criterion == null) {
            return Map.of("Without grouper", lineInfos);
        }

        return lineInfos.parallelStream()
                .collect(groupingBy(criterion));
    }

    public BigDecimal totalCost(List<BillingInfo> lineInfos) {
        return lineInfos.parallelStream()
                .map(BillingInfo::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
