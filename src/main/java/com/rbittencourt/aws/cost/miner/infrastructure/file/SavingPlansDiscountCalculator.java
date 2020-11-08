package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Collections.singletonList;

@Component
public class SavingPlansDiscountCalculator {

    public SavingPlansDiscountTable buildDiscountTable(List<BillingInfo> billingInfos) {
        Map<String, BigDecimal> discountBySubscription = new HashMap<>();

        Map<String, List<BillingInfo>> firstUseOfSavingPlansBySubscription = firstUseOfSavingPlansBySubscription(billingInfos);

        for (String subscriptionId : firstUseOfSavingPlansBySubscription.keySet()) {
            BigDecimal percentageDiscount = calculatePercentageDiscount(firstUseOfSavingPlansBySubscription, subscriptionId);
            discountBySubscription.put(subscriptionId, percentageDiscount);
        }

        return new SavingPlansDiscountTable(discountBySubscription);
    }

    private Map<String, List<BillingInfo>> firstUseOfSavingPlansBySubscription(List<BillingInfo> billingInfos) {
        Map<String, List<BillingInfo>> oneHourSampleOfSavingPlans = new HashMap<>();
        List<String> subscriptionAlreadyCalculated = new ArrayList<>();

        for (BillingInfo billingInfo : billingInfos) {
            if (subscriptionAlreadyCalculated.contains(billingInfo.getSubscriptionId())) {
                continue;
            }

            if (billingInfo.getRecordType().equals("SavingsPlanNegation")) {
                if (oneHourSampleOfSavingPlans.containsKey(billingInfo.getSubscriptionId())) {
                    oneHourSampleOfSavingPlans.get(billingInfo.getSubscriptionId()).add(billingInfo);
                } else {
                    oneHourSampleOfSavingPlans.put(billingInfo.getSubscriptionId(), new ArrayList<>(singletonList(billingInfo)));
                }

                continue;
            }

            if (billingInfo.getRecordType().equals("SavingsPlanRecurringFee")) {
                oneHourSampleOfSavingPlans.get(billingInfo.getSubscriptionId()).add(billingInfo);
                subscriptionAlreadyCalculated.add(billingInfo.getSubscriptionId());
            }
        }

        return oneHourSampleOfSavingPlans;
    }

    private BigDecimal calculatePercentageDiscount(Map<String, List<BillingInfo>> firstUseOfSavingPlansBySubscription, String subscriptionId) {
        BigDecimal savingsPlanNegationTotal = firstUseOfSavingPlansBySubscription.get(subscriptionId).stream().filter(b -> b.getRecordType().equals("SavingsPlanNegation")).map(BillingInfo::getCost).reduce(ZERO, BigDecimal::add).negate();
        BigDecimal savingsPlanRecurringFee = firstUseOfSavingPlansBySubscription.get(subscriptionId).stream().filter(b -> b.getRecordType().equals("SavingsPlanRecurringFee")).findFirst().orElseThrow().getCost();

        return BigDecimal.ONE.subtract(savingsPlanRecurringFee.divide(savingsPlanNegationTotal, 6, HALF_EVEN));
    }

}
