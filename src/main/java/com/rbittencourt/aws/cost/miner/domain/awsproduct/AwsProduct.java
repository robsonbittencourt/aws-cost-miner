package com.rbittencourt.aws.cost.miner.domain.awsproduct;

public enum AwsProduct {

    EC2("Amazon Elastic Compute Cloud"),
    RESERVED_INSTANCE("Amazon Elastic Compute Cloud");

    private String description;

    AwsProduct(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AwsProduct fromName(String name) {
        for (AwsProduct value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }

        return null;
    }

}
