package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.mask.MaskedValue;

import java.math.BigDecimal;
import java.util.Objects;

public class MetricValue {

    private String description;
    private BigDecimal value;
    private String formattedValue;

    public MetricValue(String description, String value) {
        this.description = description;
        this.formattedValue = value;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricValue that = (MetricValue) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(formattedValue, that.formattedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, formattedValue);
    }
}
