package com.rbittencourt.aws.cost.miner.domain.billing;

import com.rbittencourt.aws.cost.miner.fixture.BillingInfoFixture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class BillingQueryTest {

    @InjectMocks
    private BillingQuery billingQuery;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldFilterBillingInfosByCriteria() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EKS").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("S3").withAvailabilityZone("us-east-1").build());

        List<Predicate<BillingInfo>> criteria = new ArrayList<>();
        criteria.add(b -> b.getProductName().equals("EC2"));
        criteria.add(b -> b.getAvailabilityZone().equals("us-east-2"));

        List<BillingInfo> billingInfosFiltered = billingQuery.filter(billingInfos, criteria);

        assertEquals(2, billingInfosFiltered.size());
        assertEquals("EC2", billingInfosFiltered.get(0).getProductName());
        assertEquals("us-east-2", billingInfosFiltered.get(0).getAvailabilityZone());
        assertEquals("EC2", billingInfosFiltered.get(1).getProductName());
        assertEquals("us-east-2", billingInfosFiltered.get(1).getAvailabilityZone());
    }

    @Test
    public void shouldReturnTheSameListWhenFilterBillingInfosByCriteriaAndDoNotInformAnListOfCriterion() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withAvailabilityZone("us-east-1").build());

        List<BillingInfo> billingInfosFiltered = billingQuery.filter(billingInfos, null);

        assertEquals(2, billingInfosFiltered.size());
        assertEquals("EC2", billingInfosFiltered.get(0).getProductName());
        assertEquals("us-east-2", billingInfosFiltered.get(0).getAvailabilityZone());
        assertEquals("SQS", billingInfosFiltered.get(1).getProductName());
        assertEquals("us-east-1", billingInfosFiltered.get(1).getAvailabilityZone());
    }

    @Test
    public void shouldReturnTheSameListWhenFilterBillingInfosByCriteriaAndInformAnEmptyListOfCriterion() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withAvailabilityZone("us-east-1").build());

        List<Predicate<BillingInfo>> criteria = new ArrayList<>();

        List<BillingInfo> billingInfosFiltered = billingQuery.filter(billingInfos, criteria);

        assertEquals(2, billingInfosFiltered.size());
        assertEquals("EC2", billingInfosFiltered.get(0).getProductName());
        assertEquals("us-east-2", billingInfosFiltered.get(0).getAvailabilityZone());
        assertEquals("SQS", billingInfosFiltered.get(1).getProductName());
        assertEquals("us-east-1", billingInfosFiltered.get(1).getAvailabilityZone());
    }

    @Test
    public void shouldGroupBillingInfosByCriterion() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EKS").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("S3").withAvailabilityZone("us-east-1").build());

        Map<String, List<BillingInfo>> billingInfosGrouped = billingQuery.groupBy(billingInfos, BillingInfo::getProductName);

        assertEquals(4, billingInfosGrouped.size());
        assertEquals(3, billingInfosGrouped.get("EC2").size());
        assertEquals(1, billingInfosGrouped.get("SQS").size());
        assertEquals(1, billingInfosGrouped.get("S3").size());
        assertEquals(1, billingInfosGrouped.get("EKS").size());
    }

    @Test
    public void shouldGroupAllInOneWhenGroupBillingInfosByCriterionAndNotInformCriterion() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EKS").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-1").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withAvailabilityZone("us-east-2").build());
        billingInfos.add(BillingInfoFixture.get().withProductName("S3").withAvailabilityZone("us-east-1").build());

        Map<String, List<BillingInfo>> billingInfosGrouped = billingQuery.groupBy(billingInfos, null);

        assertEquals(1, billingInfosGrouped.size());
        assertEquals(6, billingInfosGrouped.get("Without grouper").size());
    }

    @Test
    public void shouldCalculateTotalCostFromBillingInfos() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withCost(100).build());
        billingInfos.add(BillingInfoFixture.get().withCost(125).build());
        billingInfos.add(BillingInfoFixture.get().withCost(200).build());
        billingInfos.add(BillingInfoFixture.get().withCost(412).build());

        BigDecimal totalCost = billingQuery.totalCost(billingInfos);

        assertEquals(new BigDecimal(837), totalCost);
    }

    @Test
    public void shouldReturnBillingInfosInTimePeriodBasedOnUsageStartDate() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 10, 30)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EKS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 18, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 12, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("RDS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 22, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SNS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 15, 40)).build());
        billingInfos.add(BillingInfoFixture.get().build());

        List<BillingInfo> result = billingQuery.betweenTimeRangeOfUsageStartDate(billingInfos, LocalTime.of(10, 30), LocalTime.of(16, 0));

        assertEquals(3, result.size());
        assertEquals("EC2", result.get(0).getProductName());
        assertEquals("SQS", result.get(1).getProductName());
        assertEquals("SNS", result.get(2).getProductName());
    }

    @Test
    public void shouldReturnBillingInfosInTimePeriodWithinLeapDayBasedOnUsageStartDate() {
        List<BillingInfo> billingInfos = new ArrayList<>();
        billingInfos.add(BillingInfoFixture.get().withProductName("EC2").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 19, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("EKS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 16, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SQS").withUsageStartDate(LocalDateTime.of(2019, 9, 21, 23, 30)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("RDS").withUsageStartDate(LocalDateTime.of(2019, 9, 22, 9, 0)).build());
        billingInfos.add(BillingInfoFixture.get().withProductName("SNS").withUsageStartDate(LocalDateTime.of(2019, 9, 22, 6, 40)).build());

        List<BillingInfo> result = billingQuery.betweenTimeRangeOfUsageStartDate(billingInfos, LocalTime.of(19, 0), LocalTime.of(7, 0));

        assertEquals(3, result.size());
        assertEquals("EC2", result.get(0).getProductName());
        assertEquals("SQS", result.get(1).getProductName());
        assertEquals("SNS", result.get(2).getProductName());
    }

}