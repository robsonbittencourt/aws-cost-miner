package com.rbittencourt.aws.cost.miner.domain.billing;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;

@Component
public class BillingQuery {

    @SafeVarargs
    public final List<BillingInfo> filter(List<BillingInfo> lineInfos, Predicate<BillingInfo> ...criteria) {
        return lineInfos.stream()
                .filter(stream(criteria).reduce(x -> true, Predicate::and))
                .collect(Collectors.toList());
    }

    public Map<String, List<BillingInfo>> groupBy(List<BillingInfo> lineInfos, Function<BillingInfo, String> criterion) {
        return lineInfos.stream()
                .collect(groupingBy(criterion));
    }

    public BigDecimal totalCost(List<BillingInfo> lineInfos) {
        return lineInfos.stream()
                .map(BillingInfo::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
