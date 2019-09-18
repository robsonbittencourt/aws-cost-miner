package com.rbittencourt.aws.cost.miner.domain;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.report.Report;
import com.rbittencourt.aws.cost.miner.domain.report.ec2.Ec2Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_EVEN;
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

        Predicate<BillingInfo> product = b -> ec2Report.awsProduct().equals(b.getProductName());
        Predicate<BillingInfo> considerableCost = b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0;
        List<BillingInfo> filteredBillingInfos = billingQuery.filter(billingInfos, product, considerableCost);

        writeReport(ec2Report, filteredBillingInfos);
    }

    private void writeReport(Report report, List<BillingInfo> billingInfos) {
        System.out.println("========== " + report.description() + " ==========" + "\n");

        for (Metric metric : report.metrics()) {
            if (!isEmpty(metric.description())) {
                System.out.println(metric.description());
            }

            String line = metric.calculateMetric(billingInfos);
            System.out.println(line);
        }
    }

}
