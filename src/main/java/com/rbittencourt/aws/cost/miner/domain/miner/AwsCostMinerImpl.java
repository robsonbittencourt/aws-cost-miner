package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.application.report.Report;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
class AwsCostMinerImpl implements AwsCostMiner {

    @Autowired
    private DataOrganizer dataOrganizer;

    @Autowired
    private MetricsFactory metricsFactory;

    public List<MinedData> miningCostData(Report report) {
        Map<String, BillingInfos> billingInfos = dataOrganizer.organizeData(report.buildSearchParameters());

        return mineData(report, billingInfos);
    }

    private List<MinedData> mineData(Report serviceType, Map<String, BillingInfos> groupedBillingInfos) {
        List<MinedData> minedData = new ArrayList<>();

        for (Map.Entry<String, BillingInfos> entry : groupedBillingInfos.entrySet()) {
            List<MetricResult> metricResults = getMetricResults(serviceType, entry);
            minedData.add(new MinedData(entry.getKey(), metricResults));
        }

        return minedData;
    }

    private List<MetricResult> getMetricResults(Report serviceType, Map.Entry<String, BillingInfos> entry) {
        List<MetricResult> metricResults = new ArrayList<>();

        for (Metric metric : metricsFactory.build(serviceType)) {
            MetricResult metricResult = metric.calculateMetric(entry.getValue());
            addInMetricResults(metricResults, metricResult);
        }

        return metricResults;
    }

    private void addInMetricResults(List<MetricResult> metricResults, MetricResult metricResult) {
        Optional<MetricResult> existentMetricResult = metricResults.stream()
                .filter(m -> m.getMetricName().isPresent())
                .filter(m -> m.getMetricName().equals(metricResult.getMetricName()))
                .findFirst();

        if (existentMetricResult.isPresent()) {
            existentMetricResult.get().getMetricValues().addAll(metricResult.getMetricValues());
        } else {
            metricResults.add(metricResult);
        }
    }

}
