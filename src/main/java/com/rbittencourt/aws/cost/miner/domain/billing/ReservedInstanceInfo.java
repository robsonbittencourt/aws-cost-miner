package com.rbittencourt.aws.cost.miner.domain.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReservedInstanceInfo {

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("SubscriptionId")
    private String subscriptionId;

    @JsonProperty("UsageType")
    private String usageType;

    @JsonProperty("ItemDescription")
    private String itemDescription;

    @JsonProperty("UsageQuantity")
    private BigDecimal usageQuantity;

    @JsonProperty("Rate")
    private BigDecimal rate;

    @JsonProperty("Cost")
    private BigDecimal cost;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getUsageQuantity() {
        return usageQuantity;
    }

    public void setUsageQuantity(BigDecimal usageQuantity) {
        this.usageQuantity = usageQuantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public InstanceSize instanceSize() {
        String instanceSize = usageType.substring(usageType.lastIndexOf(".") + 1);
        return InstanceSize.fromDescription(instanceSize);
    }

}
