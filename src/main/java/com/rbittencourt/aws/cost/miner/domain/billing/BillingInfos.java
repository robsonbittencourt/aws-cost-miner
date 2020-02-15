package com.rbittencourt.aws.cost.miner.domain.billing;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.*;

public class BillingInfos {

    private static final String WITHOUT_GROUPER = "Without grouper";

    private List<BillingInfo> infos;

    public BillingInfos(List<BillingInfo> infos) {
        this.infos = infos != null ? infos : new ArrayList<>();
    }

    public BillingInfos filter(List<Predicate<BillingInfo>> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return new BillingInfos(this.infos);
        }

        List<BillingInfo> filteredInfos = this.infos.parallelStream()
                .filter(criteria.parallelStream()
                        .filter(Objects::nonNull)
                        .reduce(x -> true, Predicate::and)
                )
                .collect(toList());

        return new BillingInfos(filteredInfos);
    }

    public BillingInfos filter(Predicate<BillingInfo> criterion) {
        return this.filter(Arrays.asList(criterion));
    }

    public BillingInfos filter(Predicate<BillingInfo> ...criterion) {
        return this.filter(Arrays.asList(criterion));
    }

    public Map<String, BillingInfos> groupBy(Function<BillingInfo, String> criterion) {
        if (criterion == null) {
            return Map.of(WITHOUT_GROUPER, new BillingInfos(this.infos));
        }

        Map<String, List<BillingInfo>> infosListGrouped = this.infos.parallelStream()
                .collect(groupingBy(criterion));

        return infosListGrouped.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> new BillingInfos(e.getValue())));
    }

    public BigDecimal totalCost() {
        return this.infos.parallelStream()
                .map(BillingInfo::getCost)
                .reduce(ZERO, BigDecimal::add);
    }

    public BigDecimal totalHoursUsed() {
        return this.infos.parallelStream()
                .map(BillingInfo::getUsedHours)
                .filter(Objects::nonNull)
                .reduce(ZERO, BigDecimal::add);
    }

    public BillingInfos betweenTimeRangeOfUsageStartDate(LocalTime start, LocalTime end) {
        Predicate<BillingInfo> equalsStart = b -> b.getUsageStartDate().toLocalTime().equals(start);
        Predicate<BillingInfo> afterStart = b -> b.getUsageStartDate().toLocalTime().isAfter(start);
        Predicate<BillingInfo> beforeEnd = b -> b.getUsageStartDate().toLocalTime().isBefore(end);

        Predicate<BillingInfo> criteria;
        if (start.isAfter(end)) {
            criteria = equalsStart.or(afterStart.or(beforeEnd));
        } else {
            criteria = equalsStart.or(afterStart.and(beforeEnd));
        }

        List<BillingInfo> infosInRange = this.infos.stream()
                .filter(b -> b.getUsageStartDate() != null)
                .filter(criteria)
                .collect(toList());

        return new BillingInfos(infosInRange);
    }

    public int size() {
        return this.infos.size();
    }

    public List<BillingInfo> getBillingInfos() {
        return this.infos;
    }

    public BillingInfo get(int index) {
        return this.infos.get(index);
    }

    public Stream<BillingInfo> stream() {
        return this.infos.stream();
    }

    public List<BillingInfo> reservedInstanceInfos() {
        if (this.infos.isEmpty() || this.infos.get(0).getReservedInstances().getReservedInstanceInfos().isEmpty()) {
            return new ArrayList<>();
        }

        return this.infos.get(0).getReservedInstances().getReservedInstanceInfos();
    }

}
