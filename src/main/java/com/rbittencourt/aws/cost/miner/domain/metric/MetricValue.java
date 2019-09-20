package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.mask.MaskedValue;

import java.math.BigDecimal;

public class MetricValue {

    private String description;
    private BigDecimal value;
    private MaskedValue maskedValue;

    public MetricValue(String description, BigDecimal value) {
        this.description = description;
        this.value = value;
    }

    public MetricValue(String description, BigDecimal value, MaskedValue maskedValue) {
        this.description = description;
        this.value = value;
        this.maskedValue = maskedValue;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getFormattedValue() {
        return maskedValue.getMaskedValue();
    }

    @Override
    public String toString() {
        return description + ": " + maskedValue.getMaskedValue();
    }

}
