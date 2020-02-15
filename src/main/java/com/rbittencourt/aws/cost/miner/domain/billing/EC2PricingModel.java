package com.rbittencourt.aws.cost.miner.domain.billing;

public enum EC2PricingModel {

    ON_DEMAND("On Demand"),
    SPOT_INSTANCE("Spot Instance"),
    RESERVED_INSTANCE("Reserved Instance");

    private static final String CUR_RESERVED_INSTANCES_SUBSTRING = "reserved instance applied";

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

        if (billingInfo.getReservedInstance() || billingInfo.getItemDescription().contains(CUR_RESERVED_INSTANCES_SUBSTRING)) {
            return RESERVED_INSTANCE;
        }

        throw new IllegalStateException("Pricing model can't be defined. Item description: " + billingInfo.getItemDescription());
    }

    public String getDescription() {
        return description;
    }

}
