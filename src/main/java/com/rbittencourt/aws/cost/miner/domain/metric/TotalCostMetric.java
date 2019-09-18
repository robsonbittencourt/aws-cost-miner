package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Order(0)
public class TotalCostMetric implements Metric {

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return null;
    }

    public String calculateMetric(List<BillingInfo> billingInfos) {
        BigDecimal totalCost = billingQuery.totalCost(billingInfos);

        return "Total cost: " + totalCost + "\n";
    }

}
