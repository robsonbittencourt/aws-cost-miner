package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostByInstanceFamilyTest {

    @InjectMocks
    private CostByInstanceFamily metric;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals("Cost by Instance Family", metric.description());
    }

    @Test
    public void shouldCalculateCostByInstanceFamily() {
        BillingInfo m5Large1 = BillingInfoFixture.get().withUsageType("BoxUsage:m5.large").reservedInstance().withCost(100).build();
        BillingInfo m5Large2 = BillingInfoFixture.get().withUsageType("SpotUsage:m5.large").spotInstance().withCost(200).build();
        BillingInfo m5Large3 = BillingInfoFixture.get().withUsageType("BoxUsage:m5.large").onDemand().withCost(300).build();
        BillingInfo c5Micro1 = BillingInfoFixture.get().withUsageType("SpotUsage:c5.micro").spotInstance().withCost(80).build();
        BillingInfo c5Micro2 = BillingInfoFixture.get().withUsageType("SpotUsage:c5.micro").spotInstance().withCost(20).build();
        BillingInfo c5Micro3 = BillingInfoFixture.get().withUsageType("SpotUsage:c5.micro").spotInstance().withCost(50).build();
        BillingInfo m4Medium1 = BillingInfoFixture.get().withUsageType("BoxUsage:m4.medium").onDemand().withCost(50).build();
        BillingInfo m4Medium2 = BillingInfoFixture.get().withUsageType("BoxUsage:m4.medium").onDemand().withCost(50).build();
        BillingInfo m4Medium3 = BillingInfoFixture.get().withUsageType("SpotUsage:m4.medium").spotInstance().withCost(30).build();
        BillingInfos billingInfos = new BillingInfos(List.of(m5Large1, m5Large2, m5Large3, c5Micro1, c5Micro2, c5Micro3, m4Medium1, m4Medium2, m4Medium3));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        assertEquals("m5.large", metricResult.getMetricValues().get(0).getDescription());
        assertEquals("On Demand: $300.00 (50%), Spot Instance: $200.00 (33%), Reserved Instance: $100.00 (17%), ", metricResult.getMetricValues().get(0).getFormattedValue());

        assertEquals("c5.micro", metricResult.getMetricValues().get(1).getDescription());
        assertEquals("Spot Instance: $150.00 (100%), ", metricResult.getMetricValues().get(1).getFormattedValue());

        assertEquals("m4.medium", metricResult.getMetricValues().get(2).getDescription());
        assertEquals("On Demand: $100.00 (77%), Spot Instance: $30.00 (23%), ", metricResult.getMetricValues().get(2).getFormattedValue());
    }

}