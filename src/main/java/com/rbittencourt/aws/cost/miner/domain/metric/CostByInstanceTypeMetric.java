package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(9)
@Component
@Qualifier("ec2")
public class CostByInstanceTypeMetric implements Metric {

    @Autowired
    private CostByGroup costByGroup;

    public String description() {
        return "Cost by Instance Type";
    }

    public MetricResult calculateMetric(BillingInfos billingInfos) {
        List<MetricValue> metricValues = costByGroup.metricValues(billingInfos, BillingInfo::ec2InstanceType, "Instance Type");
        return new MetricResult(description(), metricValues);
    }

}
