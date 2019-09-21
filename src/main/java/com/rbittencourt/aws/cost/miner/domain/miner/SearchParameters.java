package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class SearchParameters {

    private List<Predicate<BillingInfo>> filters = new ArrayList<>();
    private Function<BillingInfo, String> groupBy;

    public SearchParameters addFilter(Predicate<BillingInfo> filter) {
        this.filters.add(filter);
        return this;
    }

    public SearchParameters addGrouper(Function<BillingInfo, String> groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public List<Predicate<BillingInfo>> getFilters() {
        return filters;
    }

    public Function<BillingInfo, String> getGroupBy() {
        return groupBy;
    }

}
