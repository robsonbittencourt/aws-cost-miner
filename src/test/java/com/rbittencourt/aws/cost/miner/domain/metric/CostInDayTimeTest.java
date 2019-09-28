package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostInDayTimeTest {

    @InjectMocks
    private CostInDayTime metric;

    @Mock
    private BillingQuery billingQuery;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCostInNightTime() {
        BillingInfo billingDay1 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 16, 8, 0)).withCost(50).build();
        BillingInfo billingDay2 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 18, 9, 0)).withCost(50).build();
        BillingInfo billingNight1= BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 19, 20, 0)).withCost(43).build();
        BillingInfo billingNight2 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 20, 21, 0)).withCost(25).build();
        BillingInfo billingNight3 = BillingInfoFixture.get().withUsageStartDate(of(2019, 9, 21, 22, 0)).withCost(23).build();
        BillingInfo billingWithoutDate = BillingInfoFixture.get().withCost(23).build();
        List<BillingInfo> billingInfos = List.of(billingDay1, billingDay2, billingNight1, billingNight2, billingNight3, billingWithoutDate);

        when(billingQuery.betweenTimeRangeOfUsageStartDate(billingInfos, LocalTime.of(7, 0), LocalTime.of(19, 0))).thenReturn(List.of(billingDay1, billingDay2));
        when(billingQuery.totalCost(List.of(billingDay1, billingDay2))).thenReturn(new BigDecimal(100));

        MetricResult metricResult = metric.calculateMetric(billingInfos);

        MetricValue metricValue = metricResult.getMetricValues().get(0);
        assertEquals("Cost mean by day period 07:00 to 19:00", metricValue.getDescription());
        assertEquals(new BigDecimal(20), metricValue.getValue());
        assertEquals("$20.00", metricValue.getFormattedValue());

        assertEquals("Daily Metrics", metricResult.getDescription().get());
    }

}