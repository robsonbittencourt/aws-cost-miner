package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class BillingInfoCsvRepository implements BillingInfoRepository {

    @Autowired
    private CsvReader csvReader;

    @Autowired
    private ReservedInstanceInfoRepository reservedInstanceInfoRepository;

    private String userDir = System.getProperty("user.dir") + "/data.csv";

    @Override
    public BillingInfos findBillingInfos() {
        ReservedInstanceInfos reservedInstanceInfos = reservedInstanceInfoRepository.findReservedInstanceInfos();

        List<BillingInfo> billingInfos = csvReader.csvToObjects(userDir, BillingInfo.class)
                .parallelStream()
                .filter(b -> !b.getUsageType().contains("HeavyUsage"))
                .collect(toList());

        billingInfos.parallelStream().forEach(b -> b.setReservedInstances(reservedInstanceInfos));

        return new BillingInfos(billingInfos);
    }



}
