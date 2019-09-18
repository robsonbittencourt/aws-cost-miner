package com.rbittencourt.aws.cost.miner.domain.report.ec2;

import com.rbittencourt.aws.cost.miner.domain.billing.BillingInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.BillingQuery;
import com.rbittencourt.aws.cost.miner.domain.metric.Metric;
import com.rbittencourt.aws.cost.miner.domain.report.Report;
import com.rbittencourt.aws.cost.miner.infrastructure.file.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_EVEN;

@Component
public class Ec2Report implements Report {

    @Autowired
    private List<Metric> metrics;

    @Autowired
    private CsvReaderService csvReaderService;

    @Autowired
    private BillingQuery billingQuery;

    @Override
    public String description() {
        return "Cost Report - Amazon Elastic Compute Cloud (EC2)";
    }

    @Override
    public List<BillingInfo> billingLines() {
        String userDir = System.getProperty("user.dir");

        List<BillingInfo> list = csvReaderService.csvToObjects(userDir + "/data.csv", BillingInfo.class);

        Predicate<BillingInfo> considerableCost = b -> b.getCost().setScale(2, HALF_EVEN).compareTo(new BigDecimal("0.00")) > 0;
        Predicate<BillingInfo> product = b -> "Amazon Elastic Compute Cloud".equals(b.getProductName());

        return billingQuery.filter(list, considerableCost, product);
    }

    @Override
    public List<Metric> metrics() {
        return metrics;
    }

}
