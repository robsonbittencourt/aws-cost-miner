package com.rbittencourt.aws.cost.miner.domain.mask;

import java.math.BigDecimal;

public class Percent implements MaskedValue {

    private static final String PERCENT = "%";
    private static final BigDecimal HUNDRED_PERCENT = new BigDecimal("100.00");

    private BigDecimal value;

    public Percent(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String getMaskedValue() {
        if (value == null) {
            return "";
        }

        BigDecimal roundedValue = value.setScale(2);

        if (roundedValue.compareTo(HUNDRED_PERCENT) > 0) {
            return HUNDRED_PERCENT + PERCENT;
        }

        return roundedValue + PERCENT;
    }

    @Override
    public String toString() {
        return getMaskedValue();
    }

}
