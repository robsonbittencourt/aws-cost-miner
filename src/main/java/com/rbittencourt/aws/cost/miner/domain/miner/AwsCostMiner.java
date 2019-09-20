package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;

import java.util.List;
import java.util.Map;

public interface AwsCostMiner {

    Map<String, List<BillingInfo>> miningCostData();

}
