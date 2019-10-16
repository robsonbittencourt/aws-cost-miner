package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReservedInstanceUsageTest {

    @InjectMocks
    private ReservedInstanceUsage metric;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldCalculatePercentOfUsageOfReservedInstances() {
        ReservedInstanceInfo reservedInstanceInfo1 = new ReservedInstanceInfo();
        reservedInstanceInfo1.setSubscriptionId("1");
        reservedInstanceInfo1.setUsageQuantity(new BigDecimal(100));
        reservedInstanceInfo1.setUsageType("m5.16xlarge");
        reservedInstanceInfo1.setItemDescription("Reserved Instance m5.16xlarge");
        reservedInstanceInfo1.setRate(BigDecimal.TEN);

        ReservedInstanceInfo reservedInstanceInfo2 = new ReservedInstanceInfo();
        reservedInstanceInfo2.setSubscriptionId("2");
        reservedInstanceInfo2.setUsageQuantity(new BigDecimal(2000));
        reservedInstanceInfo2.setUsageType("c5.medium");
        reservedInstanceInfo2.setItemDescription("Reserved Instance c5.medium");
        reservedInstanceInfo2.setRate(BigDecimal.ONE);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(List.of(reservedInstanceInfo1, reservedInstanceInfo2));

        BillingInfo m5OnDemand = BillingInfoFixture.get().withInstanceType("m5.large").onDemand().withCost(100).withUsedHours(500).withSubscriptionId("1").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo m5Spot = BillingInfoFixture.get().withInstanceType("m5.large").spotInstance().withCost(50).withUsedHours(300).withSubscriptionId("1").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo m5Reserved = BillingInfoFixture.get().withInstanceType("m5.large").reservedInstance().withCost(50).withUsedHours(200).withSubscriptionId("1").withReservedInstances(reservedInstanceInfos).build();

        BillingInfo c5OnDemand = BillingInfoFixture.get().withInstanceType("c5.large").onDemand().withCost(25).withUsedHours(200).withSubscriptionId("2").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo c5Spot = BillingInfoFixture.get().withInstanceType("c5.large").spotInstance().withCost(75).withUsedHours(300).withSubscriptionId("2").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo c5Reserved = BillingInfoFixture.get().withInstanceType("c5.large").reservedInstance().withCost(100).withUsedHours(500).withSubscriptionId("2").withReservedInstances(reservedInstanceInfos).build();

        BillingInfos billingInfos = new BillingInfos(List.of(m5OnDemand, m5Spot, m5Reserved, c5OnDemand, c5Spot, c5Reserved));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        assertEquals("Reserved Instance Usage", metricResult.getMetricName().get());

        assertEquals("Reserved Instance m5.16xlarge", metricResult.getMetricValues().get(0).getDescription());
        assertEquals("31.25%", metricResult.getMetricValues().get(0).getFormattedValue());

        assertEquals("Reserved Instance c5.medium", metricResult.getMetricValues().get(1).getDescription());
        assertEquals("100.00%", metricResult.getMetricValues().get(1).getFormattedValue());
    }


}