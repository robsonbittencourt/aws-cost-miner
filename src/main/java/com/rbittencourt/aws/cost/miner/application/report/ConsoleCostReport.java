package com.rbittencourt.aws.cost.miner.application.report;

import com.rbittencourt.aws.cost.miner.domain.metric.MetricResult;
import com.rbittencourt.aws.cost.miner.domain.metric.MetricValue;
import com.rbittencourt.aws.cost.miner.domain.miner.AwsCostMiner;
import com.rbittencourt.aws.cost.miner.domain.miner.MinedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rbittencourt.aws.cost.miner.domain.awsservice.AwsServiceType.EC2;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class ConsoleCostReport {

    @Autowired
    private AwsCostMiner miner;

    public void writeReport() {
        List<MinedData> minedData = miner.miningCostData(EC2);

        System.out.println("========== Cost Report - " + EC2.name() + " ==========" + "\n");

        for (MinedData data : minedData) {
            String target = isEmpty(data.getTarget()) ? "Without grouper" : data.getTarget();

            System.out.println("-----------------------------------------------");
            System.out.println("| " + target + " |") ;
            System.out.println("-----------------------------------------------");

            for (MetricResult metricResult : data.getMetricResult()) {
                boolean indent = false;
                if(metricResult.getDescription().isPresent()) {
                    System.out.println(metricResult.getDescription().get());
                    indent = true;
                }

                for (MetricValue metricValue : metricResult.getMetricValues()) {
                    if (indent) {
                        System.out.print("\t");
                    }
                    System.out.println(metricValue);
                }

                System.out.println("");
            }

            System.out.println("");
        }
    }

}
