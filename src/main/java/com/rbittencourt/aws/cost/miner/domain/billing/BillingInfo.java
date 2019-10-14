package com.rbittencourt.aws.cost.miner.domain.billing;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.BooleanDeserializer;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.LocalDateTimeDeserializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class BillingInfo {

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("UsageType")
    private String usageType;

    @JsonProperty("SubscriptionId")
    private String subscriptionId;

    @JsonProperty("AvailabilityZone")
    private String availabilityZone;

    @JsonDeserialize(using = BooleanDeserializer.class)
    @JsonProperty("ReservedInstance")
    private boolean reservedInstance;

    @JsonProperty("ItemDescription")
    private String itemDescription;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("UsageStartDate")
    private LocalDateTime usageStartDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("UsageEndDate")
    private LocalDateTime usageEndDate;

    @JsonProperty("UsageQuantity")
    private BigDecimal usedHours;

    @JsonAlias({"Cost", "BlendedCost"})
    private BigDecimal cost;

    @JsonProperty("RecordType")
    private String recordType;

    private Map<String, String> otherFields = new LinkedHashMap<>();

    private ReservedInstanceInfos reservedInstances;

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

    public boolean getReservedInstance() {
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

    public BigDecimal getCost() {
        if (getReservedInstance()) {
            return reservedInstances.hourCost(subscriptionId, instanceSize()).multiply(usedHours);
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

    public boolean getSpotInstance() {
        return itemDescription!= null && itemDescription.contains("Spot Instance");
    }

    public boolean getOnDemand() {
        return itemDescription != null && itemDescription.contains("On Demand");
    }

    public boolean isEC2Instance() {
        return usageType != null && (usageType.contains("BoxUsage") || usageType.contains("SpotUsage"));
    }

    public String ec2InstanceType() {
        if (usageType.equals("BoxUsage")) {
            return "m1.small"; //for some reason AWS doesn't follow the pattern with this instance type
        } else  {
            return usageType.substring(usageType.lastIndexOf(":") + 1);
        }
    }

    public InstanceSize instanceSize() {
        String instanceSize = usageType.substring(usageType.lastIndexOf(".") + 1);
        return InstanceSize.fromDescription(instanceSize);
    }

    public EC2PricingModel ec2PricingModel() {
        return EC2PricingModel.byBillingInfo(this);
    }

}
