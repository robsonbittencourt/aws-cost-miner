package com.rbittencourt.aws.cost.miner.domain.billing;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.BooleanDeserializer;
import com.rbittencourt.aws.cost.miner.infrastructure.config.serialization.LocalDateTimeDeserializer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BillingLineInfo {

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("UsageType")
    private String usageType;

    @JsonProperty("AvailabilityZone")
    private String availabilityZone;

    @JsonDeserialize(using = BooleanDeserializer.class)
    @JsonProperty("ReservedInstance")
    private boolean reservedInstance;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("UsageStartDate")
    private LocalDateTime usageStartDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("UsageEndDate")
    private LocalDateTime usageEndDate;

    @JsonProperty("Cost")
    private BigDecimal cost;

    private Map<String, Object> otherFields = new LinkedHashMap<>();

    @JsonAnySetter
    void setOtherFields(String key, Object value) {
        if (!notMappedFields().contains(key)) {
            otherFields.put(key, value);
        }
    }

    private List<String> notMappedFields() {
        return List.of("InvoiceID", "PayerAccountId", "LinkedAccountId", "RecordType",
                "RecordId", "RateId", "SubscriptionId", "PricingPlanId", "Operation",
                "ItemDescription", "UsageQuantity", "Rate", "ResourceId");
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

    public boolean isReservedInstance() {
        return reservedInstance;
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

    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("0.0000000000");

        return "BillingLineInfo{" +
                "productName='" + productName + '\'' +
                ", usageType='" + usageType + '\'' +
                ", availabilityZone='" + availabilityZone + '\'' +
                ", reservedInstance=" + reservedInstance +
                ", usageStartDate=" + usageStartDate +
                ", usageEndDate=" + usageEndDate +
                ", cost=" + formatter.format(cost) +
                ", other=" + otherFields +
                '}';
    }
}
