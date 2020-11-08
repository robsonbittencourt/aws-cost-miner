package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SavingPlansDiscountCalculatorTest {

    @Test
    public void shouldCalculatePricesForInstancesWithSavingPlans() {
        List<BillingInfo> infos = new ArrayList<>();
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.medium").withRate(-0.0416).withCost(-0.0832).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.medium").withRate(-0.0416).withCost(-0.1664).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.medium").withRate(-0.0416).withCost(-0.0819249042).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.micro").withRate(-0.0104).withCost(-0.0208).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.small").withRate(-0.0208).withCost(-0.0208).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.small").withRate(-0.0208).withCost(-0.0208).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanNegation").withSubscriptionId("1").withUsageType("BoxUsage:t3.small").withRate(-0.0208).withCost(-0.0208).build());
        infos.add(BillingInfoFixture.get().withRecordType("SavingsPlanRecurringFee").withSubscriptionId("1").withUsageType("EC2SP:t3.1yrNoUpfront").withRate(0.26).withCost(0.26).build());

        SavingPlansDiscountCalculator calculator = new SavingPlansDiscountCalculator();
        SavingPlansDiscountTable savingPlansDiscountTable = calculator.buildDiscountTable(infos);

        assertEquals(new BigDecimal("0.373078"), savingPlansDiscountTable.discount("1"));
    }

}