package com.rbittencourt.aws.cost.miner.domain.mask;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MoneyTest {

    @Test
    public void shouldMaskValueWithDollarMask() {
        BigDecimal value = new BigDecimal(200);

        String maskedValue = new Money(value).getMaskedValue();

        assertEquals("$200.00", maskedValue);
    }

    @Test
    public void shouldReturnEmptyStringWhenValueIsNull() {
        BigDecimal value = null;

        String maskedValue = new Money(value).getMaskedValue();

        assertEquals("", maskedValue);
    }

}