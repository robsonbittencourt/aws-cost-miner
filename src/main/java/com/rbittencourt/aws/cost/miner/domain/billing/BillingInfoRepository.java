package com.rbittencourt.aws.cost.miner.domain.billing;

import java.util.List;

public interface BillingInfoRepository {

    List<BillingInfo> findBillingInfos();

}
