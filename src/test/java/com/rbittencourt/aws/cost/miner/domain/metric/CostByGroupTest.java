package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfos;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfos;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import com.rbittencourt.aws.cost.miner.infrastructure.file.SavingPlansDiscountTable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;

import static com.rbittencourt.aws.cost.miner.domain.billing.InstanceSize.LARGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CostByGroupTest {

    @InjectMocks
    private CostByGroup metric;

    @Mock
    private ReservedInstanceInfos reservedInstanceInfos;

    @Mock
    private SavingPlansDiscountTable savingPlansDiscountTable;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCostAndHoursByInstanceType() {
        BillingInfo m5OnDemand = BillingInfoFixture.get().withInstanceType("m5.large").onDemand().withCost(100).withUsedHours(500).withReservedInstances(reservedInstanceInfos).build();
        BillingInfo m5Spot = BillingInfoFixture.get().withInstanceType("m5.large").spotInstance().withCost(25).withUsedHours(200).withReservedInstances(reservedInstanceInfos).build();
        BillingInfo m5Reserved = BillingInfoFixture.get().withInstanceType("m5.large").reservedInstance().withCost(50).withUsedHours(200).withSubscriptionId("1").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo m5SavingPlans = BillingInfoFixture.get().withInstanceType("m5.large").savingPlan().withCost(-50).withUsedHours(100).withSubscriptionId("1").withSavingPlansPriceTable(savingPlansDiscountTable).build();

        BillingInfo c5OnDemand = BillingInfoFixture.get().withInstanceType("c5.large").onDemand().withCost(25).withUsedHours(200).withReservedInstances(reservedInstanceInfos).build();
        BillingInfo c5Spot = BillingInfoFixture.get().withInstanceType("c5.large").spotInstance().withCost(50).withUsedHours(200).withReservedInstances(reservedInstanceInfos).build();
        BillingInfo c5Reserved = BillingInfoFixture.get().withInstanceType("c5.large").reservedInstance().withCost(100).withUsedHours(500).withSubscriptionId("2").withReservedInstances(reservedInstanceInfos).build();
        BillingInfo c5SavingPlans = BillingInfoFixture.get().withInstanceType("c5.large").savingPlan().withCost(-50).withUsedHours(100).withSubscriptionId("2").withSavingPlansPriceTable(savingPlansDiscountTable).build();

        BillingInfos billingInfos = new BillingInfos(List.of(m5OnDemand, m5Spot, m5Reserved, m5SavingPlans, c5OnDemand, c5Spot, c5Reserved, c5SavingPlans));

        when(reservedInstanceInfos.hourCost("1", LARGE)).thenReturn(new BigDecimal("0.25"));
        when(reservedInstanceInfos.hourCost("2", LARGE)).thenReturn(new BigDecimal("0.2"));

        when(savingPlansDiscountTable.discount("1")).thenReturn(new BigDecimal("0.5"));
        when(savingPlansDiscountTable.discount("2")).thenReturn(new BigDecimal("0.5"));

        List<MetricValue> metricValues = metric.metricValues(billingInfos, BillingInfo::ec2InstanceType, "Instance Type");

        assertEquals(new MetricValue("Instance Type", "c5.large"), metricValues.get(0));
        assertEquals(new MetricValue("Pricing Model", "On Demand"), metricValues.get(1));
        assertEquals(new MetricValue("Hours", "200.0h"), metricValues.get(2));
        assertEquals(new MetricValue("Hours %", "20.00%"), metricValues.get(3));
        assertEquals(new MetricValue("Cost", "$25.00"), metricValues.get(4));
        assertEquals(new MetricValue("Cost %", "12.50%"), metricValues.get(5));

        assertEquals(new MetricValue("Instance Type", "c5.large"), metricValues.get(6));
        assertEquals(new MetricValue("Pricing Model", "Spot Instance"), metricValues.get(7));
        assertEquals(new MetricValue("Hours", "200.0h"), metricValues.get(8));
        assertEquals(new MetricValue("Hours %", "20.00%"), metricValues.get(9));
        assertEquals(new MetricValue("Cost", "$50.00"), metricValues.get(10));
        assertEquals(new MetricValue("Cost %", "25.00%"), metricValues.get(11));

        assertEquals(new MetricValue("Instance Type", "c5.large"), metricValues.get(12));
        assertEquals(new MetricValue("Pricing Model", "Reserved Instance"), metricValues.get(13));
        assertEquals(new MetricValue("Hours", "500.0h"), metricValues.get(14));
        assertEquals(new MetricValue("Hours %", "50.00%"), metricValues.get(15));
        assertEquals(new MetricValue("Cost", "$100.00"), metricValues.get(16));
        assertEquals(new MetricValue("Cost %", "50.00%"), metricValues.get(17));

        assertEquals(new MetricValue("Instance Type", "c5.large"), metricValues.get(18));
        assertEquals(new MetricValue("Pricing Model", "Savings Plan"), metricValues.get(19));
        assertEquals(new MetricValue("Hours", "100.0h"), metricValues.get(20));
        assertEquals(new MetricValue("Hours %", "10.00%"), metricValues.get(21));
        assertEquals(new MetricValue("Cost", "$25.00"), metricValues.get(22));
        assertEquals(new MetricValue("Cost %", "12.50%"), metricValues.get(23));

        assertEquals(new MetricValue("Instance Type", "c5.large"), metricValues.get(24));
        assertEquals(new MetricValue("Pricing Model", "Total"), metricValues.get(25));
        assertEquals(new MetricValue("Hours", "1000.0h"), metricValues.get(26));
        assertEquals(new MetricValue("Hours %", "100.00%"), metricValues.get(27));
        assertEquals(new MetricValue("Cost", "$200.00"), metricValues.get(28));
        assertEquals(new MetricValue("Cost %", "100.00%"), metricValues.get(29));

        assertEquals(new MetricValue("Instance Type", "m5.large"), metricValues.get(30));
        assertEquals(new MetricValue("Pricing Model", "On Demand"), metricValues.get(31));
        assertEquals(new MetricValue("Hours", "500.0h"), metricValues.get(32));
        assertEquals(new MetricValue("Hours %", "50.00%"), metricValues.get(33));
        assertEquals(new MetricValue("Cost", "$100.00"), metricValues.get(34));
        assertEquals(new MetricValue("Cost %", "50.00%"), metricValues.get(35));

        assertEquals(new MetricValue("Instance Type", "m5.large"), metricValues.get(36));
        assertEquals(new MetricValue("Pricing Model", "Spot Instance"), metricValues.get(37));
        assertEquals(new MetricValue("Hours", "200.0h"), metricValues.get(38));
        assertEquals(new MetricValue("Hours %", "20.00%"), metricValues.get(39));
        assertEquals(new MetricValue("Cost", "$25.00"), metricValues.get(40));
        assertEquals(new MetricValue("Cost %", "12.50%"), metricValues.get(41));

        assertEquals(new MetricValue("Instance Type", "m5.large"), metricValues.get(42));
        assertEquals(new MetricValue("Pricing Model", "Reserved Instance"), metricValues.get(43));
        assertEquals(new MetricValue("Hours", "200.0h"), metricValues.get(44));
        assertEquals(new MetricValue("Hours %", "20.00%"), metricValues.get(45));
        assertEquals(new MetricValue("Cost", "$50.00"), metricValues.get(46));
        assertEquals(new MetricValue("Cost %", "25.00%"), metricValues.get(47));

        assertEquals(new MetricValue("Instance Type", "m5.large"), metricValues.get(48));
        assertEquals(new MetricValue("Pricing Model", "Savings Plan"), metricValues.get(49));
        assertEquals(new MetricValue("Hours", "100.0h"), metricValues.get(50));
        assertEquals(new MetricValue("Hours %", "10.00%"), metricValues.get(51));
        assertEquals(new MetricValue("Cost", "$25.00"), metricValues.get(52));
        assertEquals(new MetricValue("Cost %", "12.50%"), metricValues.get(53));

        assertEquals(new MetricValue("Instance Type", "m5.large"), metricValues.get(54));
        assertEquals(new MetricValue("Pricing Model", "Total"), metricValues.get(55));
        assertEquals(new MetricValue("Hours", "1000.0h"), metricValues.get(56));
        assertEquals(new MetricValue("Hours %", "100.00%"), metricValues.get(57));
        assertEquals(new MetricValue("Cost", "$200.00"), metricValues.get(58));
        assertEquals(new MetricValue("Cost %", "100.00%"), metricValues.get(59));
    }

}