package com.rbittencourt.aws.cost.miner.domain.mask;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;

public class Hour implements MaskedValue {

    private static final String HOUR_REPRESENTATION = "h";

    private BigDecimal value;

    public Hour(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getMaskedValue() {
        if (value == null) {
            return "";
        }

        return value.setScale(1, HALF_EVEN) + HOUR_REPRESENTATION;
    }

    @Override
    public String toString() {
        return getMaskedValue();
    }

}
