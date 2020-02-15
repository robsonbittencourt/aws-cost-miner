package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct.EC2;
import static java.util.stream.Collectors.toList;

@Repository
public class BillingInfoCsvRepository implements BillingInfoRepository {

    private static final String USER_DIR = System.getProperty("user.dir") + "/data.csv";

    private static final String HEAVY_USAGE = "HeavyUsage";
    private static final String TAX_FOR_PRODUCT = "Tax for product";
    private static final String MB_PRICING_ADJUSTMENT = "MB - Pricing Adjustment";

    @Autowired
    private CsvReader csvReader;

    @Override
    public BillingInfos findBillingInfos() {
        List<BillingInfo> billingInfos = csvReader.csvToObjects(USER_DIR, BillingInfo.class);
        List<BillingInfo> ec2ReservedInfo = ec2ReservedInfo(billingInfos);

        ReservedInstanceInfos reservedInstanceInfos = new ReservedInstanceInfos(ec2ReservedInfo);
        List<BillingInfo> ec2InstancesInfo = ec2InstancesInfo(billingInfos, reservedInstanceInfos);

        return new BillingInfos(ec2InstancesInfo);
    }

    private List<BillingInfo> ec2ReservedInfo(List<BillingInfo> billingInfos) {
        return billingInfos.stream()
                    .filter(r -> EC2.getDescription().equals(r.getProductName()))
                    .filter(r -> r.getUsageType().contains(HEAVY_USAGE))
                    .filter(r -> !r.getItemDescription().contains(TAX_FOR_PRODUCT))
                    .filter(r -> !MB_PRICING_ADJUSTMENT.equals(r.getItemDescription()))
                    .collect(Collectors.toList());
    }

    private List<BillingInfo> ec2InstancesInfo(List<BillingInfo> billingInfos, ReservedInstanceInfos ec2ReservedInfos) {
        return billingInfos.parallelStream()
                .filter(b -> !b.getUsageType().contains(HEAVY_USAGE))
                .filter(b -> !b.getItemDescription().contains(TAX_FOR_PRODUCT))
                .peek(b -> b.setReservedInstances(ec2ReservedInfos))
                .collect(toList());
    }

}
