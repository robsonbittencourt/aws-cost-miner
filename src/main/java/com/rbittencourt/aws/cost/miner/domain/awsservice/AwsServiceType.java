package com.rbittencourt.aws.cost.miner.domain.awsservice;

public enum AwsServiceType {

    EC2("Amazon Elastic Compute Cloud");

    private String name;

    AwsServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
