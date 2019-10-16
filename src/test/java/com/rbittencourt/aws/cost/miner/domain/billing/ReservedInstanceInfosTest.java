package com.rbittencourt.aws.cost.miner.domain.billing;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReservedInstanceInfosTest {

    @Test
    public void shouldReturnNormalizedHourCostOfAnBiggerInstanceSize() {
        ReservedInstanceInfo reservedInstanceInfo1 = new ReservedInstanceInfo();
        reservedInstanceInfo1.setSubscriptionId("1");
        reservedInstanceInfo1.setUsageType("m5.medium");
        reservedInstanceInfo1.setRate(BigDecimal.TEN);

        ReservedInstanceInfo reservedInstanceInfo2 = new ReservedInstanceInfo();
        reservedInstanceInfo2.setSubscriptionId("2");
        reservedInstanceInfo2.setRate(BigDecimal.ONE);

        List<ReservedInstanceInfo> infos = List.of(reservedInstanceInfo1, reservedInstanceInfo2);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(infos);
        BigDecimal hourCost = reservedInstanceInfos.hourCost("1", InstanceSize.LARGE);

        assertEquals(new BigDecimal(20), hourCost);
    }

    @Test
    public void shouldReturnNormalizedHourCostOfAnSmallerInstanceSize() {
        ReservedInstanceInfo reservedInstanceInfo1 = new ReservedInstanceInfo();
        reservedInstanceInfo1.setSubscriptionId("1");
        reservedInstanceInfo1.setUsageType("m5.16xlarge");
        reservedInstanceInfo1.setRate(BigDecimal.TEN);

        ReservedInstanceInfo reservedInstanceInfo2 = new ReservedInstanceInfo();
        reservedInstanceInfo2.setSubscriptionId("2");
        reservedInstanceInfo2.setRate(BigDecimal.ONE);

        List<ReservedInstanceInfo> infos = List.of(reservedInstanceInfo1, reservedInstanceInfo2);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(infos);
        BigDecimal hourCost = reservedInstanceInfos.hourCost("1", InstanceSize.XLARGE);

        assertEquals(new BigDecimal(0.625).setScale(3), hourCost.setScale(3));
    }

}