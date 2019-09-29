package com.rbittencourt.aws.cost.miner.domain.billing;

public enum EC2PricingModel {

    ON_DEMAND("On Demand"),
    SPOT_INSTANCE("Spot Instance"),
    RESERVED_INSTANCE("Reserved Instance");

    private String description;

    EC2PricingModel(String description) {
        this.description = description;
    }

    public static EC2PricingModel byBillingInfo(BillingInfo billingInfo) {
        if (billingInfo.getOnDemand()) {
            return ON_DEMAND;
        }

        if (billingInfo.getSpotInstance()) {
            return SPOT_INSTANCE;
        }

        if (billingInfo.getReservedInstance()) {
            return RESERVED_INSTANCE;
        }

        return null;
    }

    public String getDescription() {
        return description;
    }

}
