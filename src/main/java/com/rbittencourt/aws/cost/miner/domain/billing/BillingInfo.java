package com.rbittencourt.aws.cost.miner.domain.billing;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.ReservedInstanceDeserializer;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.LocalDateTimeDeserializer;
import com.rbittencourt.aws.cost.miner.infrastructure.file.SavingPlansDiscountTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.math.RoundingMode.HALF_EVEN;

public class BillingInfo {

    public static final String SAVINGS_PLAN_NEGATION = "SavingsPlanNegation";

    @JsonAlias({"ProductName", "product/ProductName"})
    private String productName;

    @JsonAlias({"UsageType", "lineItem/UsageType"})
    private String usageType;

    @JsonAlias({"SubscriptionId", "reservation/SubscriptionId"})
    private String subscriptionId;

    @JsonAlias({"AvailabilityZone", "lineItem/AvailabilityZone"})
    private String availabilityZone;

    @JsonDeserialize(using = ReservedInstanceDeserializer.class)
    @JsonAlias({"ReservedInstance", "reservation/ReservationARN"})
    private boolean reservedInstance;

    @JsonAlias({"ItemDescription", "lineItem/LineItemDescription"})
    private String itemDescription;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonAlias({"UsageStartDate", "lineItem/UsageStartDate"})
    private LocalDateTime usageStartDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonAlias({"UsageEndDate", "lineItem/UsageEndDate"})
    private LocalDateTime usageEndDate;

    @JsonAlias({"UsageQuantity", "lineItem/UsageAmount"})
    private BigDecimal usedHours;

    @JsonAlias({"Rate", "BlendedRate", "lineItem/BlendedRate"})
    private BigDecimal rate;

    @JsonAlias({"Cost", "UnBlendedCost", "lineItem/UnblendedCost"})
    private BigDecimal cost;

    @JsonAlias("RecordType")
    private String recordType;

    private Map<String, String> otherFields = new LinkedHashMap<>();

    private ReservedInstanceInfos reservedInstances;

    private SavingPlansDiscountTable savingPlansDiscountTable;

    @JsonAnySetter
    void setOtherFields(String key, String value) {
        otherFields.put(key, value);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public boolean isReservedInstance() {
        return reservedInstance;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public void setReservedInstance(boolean reservedInstance) {
        this.reservedInstance = reservedInstance;
    }

    public LocalDateTime getUsageStartDate() {
        return usageStartDate;
    }

    public void setUsageStartDate(LocalDateTime usageStartDate) {
        this.usageStartDate = usageStartDate;
    }

    public LocalDateTime getUsageEndDate() {
        return usageEndDate;
    }

    public void setUsageEndDate(LocalDateTime usageEndDate) {
        this.usageEndDate = usageEndDate;
    }

    public BigDecimal getUsedHours() {
        return usedHours;
    }

    public void setUsedHours(BigDecimal usedHours) {
        this.usedHours = usedHours;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getCost() {
        if (!isReservedInstancePurchaseInfo() && isReservedInstance()) {
            return reservedInstances.hourCost(subscriptionId, instanceSize()).multiply(usedHours);
        }

        if (isSavingsPlans() && savingPlansDiscountTable != null) {
            BigDecimal savingPlanDiscount = savingPlansDiscountTable.discount(subscriptionId);
            return cost.multiply(BigDecimal.ONE.subtract(savingPlanDiscount)).setScale(4, HALF_EVEN).negate();
        }

        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public ReservedInstanceInfos getReservedInstances() {
        return reservedInstances;
    }

    public void setReservedInstances(ReservedInstanceInfos reservedInstances) {
        this.reservedInstances = reservedInstances;
    }

    public String getCustomField(String customFieldName) {
        return otherFields.get(customFieldName);
    }

    public boolean isSpotInstance() {
        return itemDescription != null && itemDescription.contains("Spot Instance");
    }

    public boolean isOnDemand() {
        return !isSavingsPlans() && itemDescription != null && itemDescription.contains("On Demand");
    }

    public boolean isSavingsPlans() {
        return SAVINGS_PLAN_NEGATION.equals(recordType);
    }

    public boolean isEC2Instance() {
        return usageType != null && (usageType.contains("BoxUsage") || usageType.contains("SpotUsage"));
    }

    public String ec2InstanceType() {
        if (usageType.contains("BoxUsage") && !usageType.contains(":")) {
            return "m1.small"; //for some reason AWS doesn't follow the pattern with this instance type
        } else  {
            return usageType.substring(usageType.lastIndexOf(":") + 1);
        }
    }

    public String instanceFamily() {
        String instanceType = ec2InstanceType();

        try {
            return instanceType.substring(0, instanceType.lastIndexOf("."));
        } catch(Exception e) {
            return "Unknown";
        }
    }

    public InstanceSize instanceSize() {
        String instanceSize = usageType.substring(usageType.lastIndexOf(".") + 1);
        return InstanceSize.fromDescription(instanceSize);
    }

    public EC2PricingModel ec2PricingModel() {
        return EC2PricingModel.byBillingInfo(this);
    }

    public boolean isReservedInstancePurchaseInfo() {
        return usageType != null && usageType.contains("HeavyUsage");
    }

    public void setSavingPlansDiscountTable(SavingPlansDiscountTable savingPlansDiscountTable) {
        this.savingPlansDiscountTable = savingPlansDiscountTable;
    }

}
