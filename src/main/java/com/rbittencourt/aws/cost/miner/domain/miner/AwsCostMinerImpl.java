package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.awsservice.ec2.EC2;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

@Component
class AwsCostMinerImpl implements AwsCostMiner {

    @Autowired
    private BillingInfoRepository repository;

    @Autowired
    private BillingQuery billingQuery;

    @Autowired
    private EC2 ec2Report;

    public Map<String, List<BillingInfo>> miningCostData() {
        List<BillingInfo> billingInfos = repository.findBillingInfos();

        List<BillingInfo> filteredBillingInfos = filterBillingInfos(billingInfos);

        return groupBillingInfos(filteredBillingInfos);
    }

    private List<BillingInfo> filterBillingInfos(List<BillingInfo> billingInfos) {
        Predicate<BillingInfo> product = b -> ec2Report.awsProduct().equals(b.getProductName());
        Predicate<BillingInfo> considerableCost = b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0;

        return billingQuery.filter(billingInfos, product, considerableCost);
    }

    private Map<String, List<BillingInfo>> groupBillingInfos(List<BillingInfo> filteredBillingInfos) {
        Map<String, List<BillingInfo>> groupedBillingInfos = billingQuery.groupBy(filteredBillingInfos, b -> b.getCustomField("user:Name"));

        return groupedBillingInfos.entrySet().stream()
                .sorted(comparing(v -> billingQuery.totalCost(v.getValue()), reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
