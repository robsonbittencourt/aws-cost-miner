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

public class CostMeanByDayTest {

    @InjectMocks
    private CostMeanByDay metric;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCostMeanByDay() {
        BillingInfo billingMonday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 16, 8, 0)).withCost(100).build();
        BillingInfo billingThursday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 17, 8, 0)).withCost(300).build();
        BillingInfo billingWednesday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 18, 8, 0)).withCost(50).build();
        BillingInfo billingTuesday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 19, 8, 0)).withCost(50).build();
        BillingInfo billingFriday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 20, 8, 0)).withCost(75).build();
        BillingInfo billingSaturday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 21, 8, 0)).withCost(50).build();
        BillingInfo billingSunday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 22, 8, 0)).withCost(75).build();
        BillingInfo billingWithoutDate = BillingInfoFixture.get().withCost(23).build();
        BillingInfos billingInfos = new BillingInfos(List.of(billingMonday, billingThursday, billingWednesday, billingTuesday, billingFriday, billingSaturday, billingSunday, billingWithoutDate));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        MetricValue metricValue = metricResult.getMetricValues().get(0);
        assertEquals("Cost mean by day", metricValue.getDescription());
        assertEquals(new BigDecimal(100), metricValue.getValue());
        assertEquals("$100.00", metricValue.getFormattedValue());

        assertEquals("Daily Metrics", metricResult.getMetricName().get());
    }

}