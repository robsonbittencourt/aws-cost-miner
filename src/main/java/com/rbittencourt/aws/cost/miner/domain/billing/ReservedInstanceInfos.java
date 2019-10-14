package com.rbittencourt.aws.cost.miner.domain.billing;

import java.math.BigDecimal;
import java.util.List;

public class ReservedInstanceInfos {

    private List<ReservedInstanceInfo> reservedInstanceInfos;

    public ReservedInstanceInfos(List<ReservedInstanceInfo> reservedInstanceInfos) {
        this.reservedInstanceInfos = reservedInstanceInfos;
    }

    public List<ReservedInstanceInfo> getReservedInstanceInfos() {
        return reservedInstanceInfos;
    }

    public BigDecimal hourCost(String subscriptionId, InstanceSize instanceSize) {
        ReservedInstanceInfo reservedInfo = reservedInstanceInfos.stream()
                                                         .filter(r -> r.getSubscriptionId().equals(subscriptionId))
                                                         .findFirst()
                                                         .orElseThrow(() -> new RuntimeException("Invalid subscription id found: " + subscriptionId));

        BigDecimal normalizedFactor = instanceSize.normalize(reservedInfo.instanceSize());

        return reservedInfo.getRate().multiply(normalizedFactor);
    }

}
