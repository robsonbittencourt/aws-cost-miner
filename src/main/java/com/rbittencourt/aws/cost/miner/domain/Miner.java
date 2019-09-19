package com.rbittencourt.aws.cost.miner.domain;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.report.ec2.Ec2Report;
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
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class Miner {

    @Autowired
    private BillingInfoRepository repository;

    @Autowired
    private BillingQuery billingQuery;

    @Autowired
    private Ec2Report ec2Report;

    public void miningData() {
        List<BillingInfo> billingInfos = repository.findBillingInfos();

        List<BillingInfo> filteredBillingInfos = filterBillingInfos(billingInfos);

        Map<String, List<BillingInfo>> groupedBillingInfosOrdered = groupBillingInfos(filteredBillingInfos);

        writeReport(groupedBillingInfosOrdered);
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

    private void writeReport(Map<String, List<BillingInfo>> groupedBillingInfos) {
        System.out.println("========== " + ec2Report.description() + " ==========" + "\n");

        for (Map.Entry<String, List<BillingInfo>> infos : groupedBillingInfos.entrySet()) {
            System.out.println(infos.getKey());
            System.out.println("--------------------------------------");

            for (Metric metric : ec2Report.metrics()) {
                if (!isEmpty(metric.description())) {
                    System.out.println(metric.description());
                }

                String line = metric.calculateMetric(infos.getValue());
                System.out.println(line);
            }

            System.out.println("");
        }


    }

}
