package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsServiceType;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.math.RoundingMode.HALF_EVEN;

@Component
class AwsCostMinerImpl implements AwsCostMiner {

    @Autowired
    private DataOrganizer dataOrganizer;

    @Autowired
    private MetricsFactory metricsFactory;

    public List<MinedData> miningCostData(AwsServiceType serviceType) {
        SearchParameters searchParameters = new SearchParameters()
                .addFilter(b -> serviceType.getName().equals(b.getServiceName()))
                .addFilter(b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0)
                .addGrouper(b -> b.getCustomField("user:Name"));

        Map<String, List<BillingInfo>> billingInfos = dataOrganizer.organizeData(searchParameters);

        return minedData(serviceType, billingInfos);
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
