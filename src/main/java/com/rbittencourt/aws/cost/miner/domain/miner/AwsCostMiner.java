package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct;

import java.util.List;

public interface AwsCostMiner {

    List<MinedData> miningCostData(AwsProduct service, SearchParameters searchParameters);

}
