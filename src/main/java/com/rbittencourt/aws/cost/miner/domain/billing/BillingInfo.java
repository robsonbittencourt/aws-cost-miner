package com.rbittencourt.aws.cost.miner.domain.billing;

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

    @JsonProperty("Cost")
    private BigDecimal cost;

    @JsonProperty("RecordType")
    private String recordType;

    private Map<String, String> otherFields = new LinkedHashMap<>();

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

    public BigDecimal getCost() {
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

    public String ec2InstanceFamily() {
        try {
            return usageType.substring(usageType.lastIndexOf(":") + 1);
        } catch (Exception e) {
            return "m1.small"; //TODO
        }
    }

    public EC2PricingModel ec2PricingModel() {
        return EC2PricingModel.byBillingInfo(this);
    }

}
