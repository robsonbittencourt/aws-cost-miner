package com.rbittencourt.aws.cost.miner.domain.awsproduct;

public enum AwsProduct {

    EC2("Amazon Elastic Compute Cloud"),
    SQS("Amazon Simple Queue Service");

    private String name;

    AwsProduct(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
