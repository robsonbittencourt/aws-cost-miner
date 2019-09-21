package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toMap;

@Component
class DataOrganizer {

    @Autowired
    private BillingInfoRepository repository;

    @Autowired
    private BillingQuery billingQuery;

    Map<String, List<BillingInfo>> organizeData(SearchParameters parameters) {
        List<BillingInfo> billingInfos = repository.findBillingInfos();

        List<BillingInfo> filteredBillingInfos = billingQuery.filter(billingInfos, parameters.getFilters());

        Map<String, List<BillingInfo>> groupedBillingInfos = billingQuery.groupBy(filteredBillingInfos, parameters.getGroupBy());

        return sort(groupedBillingInfos);
    }

    private Map<String, List<BillingInfo>> sort(Map<String, List<BillingInfo>> groupedBillingInfos) {
        return groupedBillingInfos.entrySet().parallelStream()
                .sorted(comparing(v -> billingQuery.totalCost(v.getValue()), reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
