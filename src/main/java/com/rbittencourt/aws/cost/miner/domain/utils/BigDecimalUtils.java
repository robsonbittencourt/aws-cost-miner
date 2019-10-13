package com.rbittencourt.aws.cost.miner.domain.utils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;

public class BigDecimalUtils {

    public static BigDecimal percentOf(BigDecimal part, BigDecimal total) {
        if (total.compareTo(ZERO) == 0) {
            return ZERO.setScale(2);
        }

        return part.divide(total, 6, HALF_EVEN).multiply(new BigDecimal(100)).setScale(2, HALF_EVEN);
    }

}
