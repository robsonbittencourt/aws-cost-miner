package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostInNightTimeTest {

    @InjectMocks
    private CostInNightTime metric;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCostInNightTime() {
        BillingInfo billingDay1 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 16, 8, 0)).withCost(43).build();
        BillingInfo billingDay2 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 18, 9, 0)).withCost(37).build();
        BillingInfo billingNight1= BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 19, 20, 0)).withCost(75).build();
        BillingInfo billingNight2 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 20, 21, 0)).withCost(75).build();
        BillingInfo billingNight3 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 21, 22, 0)).withCost(150).build();
        BillingInfo billingWithoutDate = BillingInfoFixture.get().withCost(23).build();
        BillingInfos billingInfos = new BillingInfos(List.of(billingDay1, billingDay2, billingNight1, billingNight2, billingNight3, billingWithoutDate));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        MetricValue metricValue = metricResult.getMetricValues().get(0);
        assertEquals("Cost mean by day period 19:00 to 07:00", metricValue.getDescription());
        assertEquals(new BigDecimal(60), metricValue.getValue());
        assertEquals("$60.00", metricValue.getFormattedValue());

        assertEquals("Daily Metrics", metricResult.getMetricName().get());
    }

}