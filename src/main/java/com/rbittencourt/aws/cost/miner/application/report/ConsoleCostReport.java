package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.miner.AwsCostMiner;
import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsProduct;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ConsoleCostReport {

    @Autowired
    private AwsCostMiner miner;

    @Autowired
    private AwsProduct awsProduct;

    public void writeReport() {
        Map<String, List<BillingInfo>> groupedBillingInfos = miner.miningCostData();

        System.out.println("========== " + awsProduct.description() + " ==========" + "\n");

        for (Map.Entry<String, List<BillingInfo>> infos : groupedBillingInfos.entrySet()) {
            System.out.println(infos.getKey());
            System.out.println("--------------------------------------");

            for (Metric metric : awsProduct.metrics()) {
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
