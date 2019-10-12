package com.rbittencourt.aws.cost.miner.domain.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static com.rbittencourt.aws.cost.miner.domain.utils.BigDecimalUtils.percentOf;
import static org.junit.Assert.*;

public class BigDecimalUtilsTest {

    @Test
    public void shouldReturn50PercentOfTotal() {
        BigDecimal percent = percentOf(new BigDecimal(50), new BigDecimal(100));

        assertEquals(new BigDecimal(50.00).setScale(2), percent);
    }

    @Test
    public void shouldReturn10PercentOfTotal() {
        BigDecimal percent = percentOf(new BigDecimal(53.5), new BigDecimal(535));

        assertEquals(new BigDecimal(10.00).setScale(2), percent);
    }

    @Test
    public void shouldReturn23PercentOfTotal() {
        BigDecimal percent = percentOf(new BigDecimal(541), new BigDecimal(2352));

        assertEquals(new BigDecimal(23.00).setScale(2), percent);
    }

    @Test
    public void shouldReturnZeroWhenTotalIsZero() {
        BigDecimal percent = percentOf(new BigDecimal(500), BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO.setScale(2), percent);
    }

    @Test
    public void shpuldNotConsiderScaleToCompareToZero() {
        BigDecimal percent = percentOf(new BigDecimal(500), new BigDecimal(0.00));

        assertEquals(BigDecimal.ZERO.setScale(2), percent);
    }

    @Test
    public void shouldReturnZeroWhenPartIsZero() {
        BigDecimal percent = percentOf(BigDecimal.ZERO, new BigDecimal(500));

        assertEquals(BigDecimal.ZERO.setScale(2), percent);
    }

}