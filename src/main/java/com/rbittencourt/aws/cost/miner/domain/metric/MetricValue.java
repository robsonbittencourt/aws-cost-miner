package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.mask.MaskedValue;

import java.math.BigDecimal;

public class MetricValue {

    private String description;
    private BigDecimal value;
    private String formattedValue;

    public MetricValue(String description, BigDecimal value, MaskedValue maskedValue) {
        this.description = description;
        this.value = value;
        this.formattedValue = maskedValue.getMaskedValue();
    }

    public MetricValue(String description, BigDecimal value, String formattedValue) {
        this.description = description;
        this.value = value;
        this.formattedValue = formattedValue;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    @Override
    public String toString() {
        return description + ": " + formattedValue;
    }

}
