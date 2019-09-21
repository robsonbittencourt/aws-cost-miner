package com.rbittencourt.aws.cost.miner.domain.metric;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct.EC2;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class MetricsFactoryTest {

    @InjectMocks
    private MetricsFactory factory;

    @Mock
    private List<Metric> metrics;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldReturnAllMetricsWhenProductIsNull() {
        List<Metric> result = factory.build(null);

        assertEquals(metrics, result);
    }

    @Test
    public void shouldReturnAllMetricsWhenProductIsEC2() {
        List<Metric> result = factory.build(EC2);

        assertEquals(metrics, result);
    }

}