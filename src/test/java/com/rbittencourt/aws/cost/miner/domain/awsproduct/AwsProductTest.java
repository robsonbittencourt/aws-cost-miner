package com.rbittencourt.aws.cost.miner.domain.awsproduct;

import org.junit.Test;

import static org.junit.Assert.*;

public class AwsProductTest {

    @Test
    public void shouldReturnAwsProductByName() {
        String name = "EC2";

        AwsProduct awsProduct = AwsProduct.fromName(name);

        assertEquals(AwsProduct.EC2, awsProduct);
    }
}