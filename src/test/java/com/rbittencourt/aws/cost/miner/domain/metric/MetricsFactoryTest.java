package com.rbittencourt.aws.cost.miner.domain.metric;

import com.rbittencourt.aws.cost.miner.application.report.EC2CostReport;
import com.rbittencourt.aws.cost.miner.application.report.ReservedInstanceUsageReport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.rbittencourt.aws.cost.miner.application.report.ReportType.EC2_COST_REPORT;
import static com.rbittencourt.aws.cost.miner.application.report.ReportType.RESERVED_INSTANCE_USAGE_REPORT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MetricsFactoryTest {

    @InjectMocks
    private MetricsFactory factory;

    @Mock
    private List<Metric> ec2Metrics;

    @Mock
    private List<Metric> reservedInstanceMetrics;

    @Mock
    private EC2CostReport ec2CostReport;

    @Mock
    private ReservedInstanceUsageReport reservedInstanceUsageReport;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenReportIsNull() {
        factory.build(null);
    }

    @Test
    public void shouldReturnEc2MetricsWhenReportIsEC2() {
        when(ec2CostReport.reportType()).thenReturn(EC2_COST_REPORT);

        List<Metric> result = factory.build(ec2CostReport);

        assertEquals(ec2Metrics, result);
    }

    @Test
    public void shouldReturnReservedInstanceMetricsWhenReportIsAboutReservedInstances() {
        when(reservedInstanceUsageReport.reportType()).thenReturn(RESERVED_INSTANCE_USAGE_REPORT);

        List<Metric> result = factory.build(reservedInstanceUsageReport);

        assertEquals(reservedInstanceMetrics, result);
    }

}