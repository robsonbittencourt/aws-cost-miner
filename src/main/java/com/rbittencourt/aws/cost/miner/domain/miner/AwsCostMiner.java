package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsServiceType;

import java.util.List;

public interface AwsCostMiner {

    List<MinedData> miningCostData(AwsServiceType service);

}
