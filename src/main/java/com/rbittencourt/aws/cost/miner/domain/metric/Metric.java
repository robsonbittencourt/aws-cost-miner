package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;

public interface Metric {

    String description();

    MetricResult calculateMetric(BillingInfos billingInfos);

}
