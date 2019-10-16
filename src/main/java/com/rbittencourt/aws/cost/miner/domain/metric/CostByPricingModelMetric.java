package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(7)
@Component
@Qualifier("ec2")
public class CostByPricingModelMetric implements Metric {

    @Autowired
    private CostByGroup costByGroup;

    public String description() {
        return "Cost by Pricing Model";
    }

    public MetricResult calculateMetric(BillingInfos billingInfos) {
        List<MetricValue> metricValues = costByGroup.metricValues(billingInfos, null, null);
        return new MetricResult(description(), metricValues);
    }

}
