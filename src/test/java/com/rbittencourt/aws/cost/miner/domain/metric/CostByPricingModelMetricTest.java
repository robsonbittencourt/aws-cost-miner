package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostByPricingModelMetricTest {

    @InjectMocks
    private CostByPricingModelMetric metric;

    @Mock
    private CostByGroup costByGroup;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals("Cost by Pricing Model", metric.description());
    }

    @Test
    public void shouldCalculateCostAndHoursByPricingModel() {
        BillingInfo m5OnDemand = BillingInfoFixture.get().withInstanceType("m5.large").onDemand().withCost(100).withUsedHours(500).build();
        BillingInfo m5Spot = BillingInfoFixture.get().withInstanceType("m5.large").spotInstance().withCost(50).withUsedHours(300).build();
        BillingInfo m5Reserved = BillingInfoFixture.get().withInstanceType("m5.large").reservedInstance().withCost(50).withUsedHours(200).build();
        BillingInfos billingInfos = new BillingInfos(List.of(m5OnDemand, m5Spot, m5Reserved));

        List<MetricValue> valuesResponse = List.of(new MetricValue("test", "test"));
        when(costByGroup.metricValues(billingInfos, null, null)).thenReturn(valuesResponse);

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        assertEquals("Cost by Pricing Model", metricResult.getMetricName().get());
        assertEquals(valuesResponse, metricResult.getMetricValues());
    }

}