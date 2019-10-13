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

public class CostMeanInWeekdaysTest {

    @InjectMocks
    private CostMeanInWeekdays metric;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCostMeanInWeekdays() {
        BillingInfo billingMonday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 16, 8, 0)).withCost(20).build();
        BillingInfo billingThursday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 17, 8, 0)).withCost(15).build();
        BillingInfo billingWednesday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 18, 8, 0)).withCost(15).build();
        BillingInfo billingTuesday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 19, 8, 0)).withCost(30).build();
        BillingInfo billingFriday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 20, 8, 0)).withCost(20).build();
        BillingInfo billingSaturday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 21, 8, 0)).withCost(43).build();
        BillingInfo billingSunday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 22, 8, 0)).withCost(57).build();
        BillingInfo billingWithoutDate = BillingInfoFixture.get().withCost(23).build();
        BillingInfos billingInfos = new BillingInfos(List.of(billingMonday, billingThursday, billingWednesday, billingTuesday, billingFriday, billingSaturday, billingSunday, billingWithoutDate));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        MetricValue metricValue = metricResult.getMetricValues().get(0);
        assertEquals("Cost mean in weekdays", metricValue.getDescription());
        assertEquals(new BigDecimal(20), metricValue.getValue());
        assertEquals("$20.00", metricValue.getFormattedValue());

        assertEquals("Daily Metrics", metricResult.getMetricName().get());
    }

    @Test
    public void shouldReturnZeroWhenNotHaveWeekdays() {
        BillingInfo billingSaturday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 21, 8, 0)).withCost(43).build();
        BillingInfo billingSunday = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 22, 8, 0)).withCost(57).build();
        BillingInfos billingInfos = new BillingInfos(List.of(billingSaturday, billingSunday));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        MetricValue metricValue = metricResult.getMetricValues().get(0);
        assertEquals(new BigDecimal(0), metricValue.getValue());
        assertEquals("$0.00", metricValue.getFormattedValue());
    }

}