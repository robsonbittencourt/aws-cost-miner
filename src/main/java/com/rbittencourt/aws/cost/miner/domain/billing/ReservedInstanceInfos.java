package com.rbittencourt.aws.cost.miner.domain.billing;

import java.math.BigDecimal;
import java.util.List;

public class ReservedInstanceInfos {

    private List<BillingInfo> reservedInstanceInfos;

    public ReservedInstanceInfos(List<BillingInfo> reservedInstanceInfos) {
        this.reservedInstanceInfos = reservedInstanceInfos;
    }

    public List<BillingInfo> getReservedInstanceInfos() {
        return reservedInstanceInfos;
    }

    public BigDecimal hourCost(String subscriptionId, InstanceSize instanceSize) {
        BillingInfo reservedInfo = reservedInstanceInfos.stream()
                                                         .filter(r -> r.getSubscriptionId().equals(subscriptionId))
                                                         .findFirst()
                                                         .orElseThrow(() -> new RuntimeException("Invalid subscription id found: " + subscriptionId));

        BigDecimal normalizedFactor = instanceSize.normalize(reservedInfo.instanceSize());

        return reservedInfo.getRate().multiply(normalizedFactor);
    }

}
