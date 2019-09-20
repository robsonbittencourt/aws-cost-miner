package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsServiceType;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private MetricsFactory metricsFactory;

    public List<MinedData> miningCostData(AwsServiceType serviceType) {
        List<BillingInfo> billingInfos = repository.findBillingInfos();

        List<BillingInfo> filteredBillingInfos = filterBillingInfos(serviceType, billingInfos);

        Map<String, List<BillingInfo>> groupedBillingInfos = groupBillingInfos(filteredBillingInfos);

        return minedData(serviceType, groupedBillingInfos);
    }

    private List<BillingInfo> filterBillingInfos(AwsServiceType serviceType, List<BillingInfo> billingInfos) {
        Predicate<BillingInfo> serviceFilter = b -> serviceType.getName().equals(b.getServiceName());
        Predicate<BillingInfo> considerableCostFilter = b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0;

        return billingQuery.filter(billingInfos, serviceFilter, considerableCostFilter);
    }

    private Map<String, List<BillingInfo>> groupBillingInfos(List<BillingInfo> filteredBillingInfos) {
        Map<String, List<BillingInfo>> groupedBillingInfos = billingQuery.groupBy(filteredBillingInfos, b -> b.getCustomField("user:Name"));

        return groupedBillingInfos.entrySet().parallelStream()
                .sorted(comparing(v -> billingQuery.totalCost(v.getValue()), reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private List<MinedData> minedData(AwsServiceType serviceType, Map<String, List<BillingInfo>> groupedBillingInfos) {
        List<MinedData> minedData = new ArrayList<>();

        for (Map.Entry<String, List<BillingInfo>> entry : groupedBillingInfos.entrySet()) {
            List<MetricResult> metricResults = new ArrayList<>();

            for (Metric metric : metricsFactory.build(serviceType)) {
                MetricResult metricResult = metric.calculateMetric(entry.getValue());
                metricResults.add(metricResult);
            }

            minedData.add(new MinedData(entry.getKey(), metricResults));
        }

        return minedData;
    }

}
