package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.StringUtils.isEmpty;

@Component
class DataOrganizer {

    private static final String ROUNDING = "Rounding";

    @Autowired
    private BillingInfoRepository repository;

    Map<String, BillingInfos> organizeData(SearchParameters parameters) {
        BillingInfos billingInfos = repository.findBillingInfos();

        parameters.addFilter(b -> !isEmpty(b.getProductName()) || ROUNDING.equals(b.getRecordType()));
        BillingInfos filteredBillingInfos = billingInfos.filter(parameters.getFilters());

        Map<String, BillingInfos> groupedBillingInfos = filteredBillingInfos.groupBy(parameters.getGroupBy());

        return sort(groupedBillingInfos);
    }

    private Map<String, BillingInfos> sort(Map<String, BillingInfos> groupedBillingInfos) {
        return groupedBillingInfos.entrySet().parallelStream()
                .sorted(comparing(v -> v.getValue().totalCost(), reverseOrder()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
