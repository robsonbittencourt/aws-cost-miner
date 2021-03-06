package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostByInstanceTypeMetricTest {

    @InjectMocks
    private CostByInstanceTypeMetric metric;

    @Mock
    private CostByGroup costByGroup;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals("Cost by Instance Type", metric.description());
    }

    @Test
    public void shouldCalculateCostAndHoursByInstanceType() {
        BillingInfo m5OnDemand = BillingInfoFixture.get().withInstanceType("m5.large").onDemand().withCost(100).withUsedHours(500).build();
        BillingInfo m5Spot = BillingInfoFixture.get().withInstanceType("m5.large").spotInstance().withCost(50).withUsedHours(300).build();
        BillingInfo m5Reserved = BillingInfoFixture.get().withInstanceType("m5.large").reservedInstance().withCost(50).withUsedHours(200).build();
        BillingInfos billingInfos = new BillingInfos(List.of(m5OnDemand, m5Spot, m5Reserved));

        List<MetricValue> valuesResponse = List.of(new MetricValue("test", "test"));
        when(costByGroup.metricValues(eq(billingInfos), any(), Mockito.eq("Instance Type"))).thenReturn(valuesResponse);

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        assertEquals("Cost by Instance Type", metricResult.getMetricName().get());
        assertEquals(valuesResponse, metricResult.getMetricValues());
    }

}