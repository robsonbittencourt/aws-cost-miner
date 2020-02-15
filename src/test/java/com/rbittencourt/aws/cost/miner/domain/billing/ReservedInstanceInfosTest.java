package com.rbittencourt.aws.cost.miner.domain.billing;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReservedInstanceInfosTest {

    @Test
    public void shouldReturnNormalizedHourCostOfAnBiggerInstanceSize() {
        BillingInfo reservedInstanceInfo1 = new BillingInfo();
        reservedInstanceInfo1.setSubscriptionId("1");
        reservedInstanceInfo1.setUsageType("m5.medium");
        reservedInstanceInfo1.setRate(BigDecimal.TEN);

        BillingInfo reservedInstanceInfo2 = new BillingInfo();
        reservedInstanceInfo2.setSubscriptionId("2");
        reservedInstanceInfo2.setRate(BigDecimal.ONE);

        List<BillingInfo> infos = List.of(reservedInstanceInfo1, reservedInstanceInfo2);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(infos);
        BigDecimal hourCost = reservedInstanceInfos.hourCost("1", InstanceSize.LARGE);

        assertEquals(new BigDecimal(20), hourCost);
    }

    @Test
    public void shouldReturnNormalizedHourCostOfAnSmallerInstanceSize() {
        BillingInfo reservedInstanceInfo1 = new BillingInfo();
        reservedInstanceInfo1.setSubscriptionId("1");
        reservedInstanceInfo1.setUsageType("m5.16xlarge");
        reservedInstanceInfo1.setRate(BigDecimal.TEN);

        BillingInfo reservedInstanceInfo2 = new BillingInfo();
        reservedInstanceInfo2.setSubscriptionId("2");
        reservedInstanceInfo2.setRate(BigDecimal.ONE);

        List<BillingInfo> infos = List.of(reservedInstanceInfo1, reservedInstanceInfo2);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(infos);
        BigDecimal hourCost = reservedInstanceInfos.hourCost("1", InstanceSize.XLARGE);

        assertEquals(new BigDecimal("0.625").setScale(3), hourCost.setScale(3));
    }

}