package com.rbittencourt.aws.cost.miner.domain.billing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BillingInfoTest {

    @Test
    public void shouldReturnInstanceTypeFromUsageType() {
        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setUsageType("BoxUsage:m5.large");

        String instanceType = billingInfo.ec2InstanceType();

        assertEquals("m5.large", instanceType);
    }

    @Test
    public void shouldReturnM1SmallWhenNotHaveBoxUsage() {
        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setUsageType("USW2-BoxUsage");

        String instanceType = billingInfo.ec2InstanceType();

        assertEquals("m1.small", instanceType);
    }

}