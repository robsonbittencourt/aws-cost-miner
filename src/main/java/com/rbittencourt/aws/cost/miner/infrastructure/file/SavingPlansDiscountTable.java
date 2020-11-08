package com.rbittencourt.aws.cost.miner.infrastructure.file;

import java.math.BigDecimal;
import java.util.Map;

public class SavingPlansDiscountTable {

    private Map<String, BigDecimal> discountTable;

    SavingPlansDiscountTable(Map<String, BigDecimal> discountTable) {
        this.discountTable = discountTable;
    }

    public BigDecimal discount(String subscriptionId) {
        return discountTable.get(subscriptionId);
    }

}
