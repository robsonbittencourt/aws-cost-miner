package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.rbittencourt.aws.cost.miner.domain.awsproduct.AwsProduct.EC2;

@Repository
public class ReservedInstanceInfoCsvRepository implements ReservedInstanceInfoRepository {

    private static final String RI_USAGE_TYPE = "HeavyUsage";
    private static final String MB_PRICING_ADJUSTMENT = "MB - Pricing Adjustment";

    @Autowired
    private CsvReader csvReader;

    private String userDir = System.getProperty("user.dir") + "/data.csv";

    @Override
    public ReservedInstanceInfos findReservedInstanceInfos() {
        Predicate<ReservedInstanceInfo> conditionToStop = reserved -> reserved.getUsageType() != null && reserved.getUsageType().contains(RI_USAGE_TYPE);
        List<ReservedInstanceInfo> reservedInstanceInfos = csvReader.csvToObjects(userDir, ReservedInstanceInfo.class, conditionToStop);

        List<ReservedInstanceInfo> ec2ReservedInfo = reservedInstanceInfos.stream()
                .filter(r -> EC2.getDescription().equals(r.getProductName()))
                .filter(r -> !MB_PRICING_ADJUSTMENT.equals(r.getItemDescription()))
                .collect(Collectors.toList());

        return new ReservedInstanceInfos(ec2ReservedInfo);
    }

}
