package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;

import java.util.List;

public interface Metric {

    String description();

    String calculateMetric(List<BillingInfo> billingInfos);

}
