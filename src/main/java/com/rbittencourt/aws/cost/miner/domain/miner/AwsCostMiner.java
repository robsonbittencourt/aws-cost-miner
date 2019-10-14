package com.rbittencourt.aws.cost.miner.domain.miner;

import com.rbittencourt.aws.cost.miner.application.report.Report;

import java.util.List;

public interface AwsCostMiner {

    List<MinedData> miningCostData(Report report);

}
