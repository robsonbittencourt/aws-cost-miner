package com.rbittencourt.aws.cost.miner.domain.mask;

import java.math.BigDecimal;

public class Percent implements MaskedValue {

    private static final String PERCENT = "%";

    private BigDecimal value;

    public Percent(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getMaskedValue() {
        if (value == null) {
            return "";
        }

        return value.setScale(2) + PERCENT;
    }

    @Override
    public String toString() {
        return getMaskedValue();
    }

}
