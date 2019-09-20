package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.domain.awsservice.AwsServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsFactory {

    @Autowired
    private List<Metric> metrics;

    public List<Metric> build(AwsServiceType serviceType) {
        if (AwsServiceType.EC2.equals(serviceType)) {
            return metrics;
        }

        throw new IllegalArgumentException(String.format("Service type %s is not valid", serviceType.getClass().toString()));
    }

}
