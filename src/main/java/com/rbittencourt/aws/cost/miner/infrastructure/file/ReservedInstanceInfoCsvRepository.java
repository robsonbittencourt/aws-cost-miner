package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfo;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfoRepository;
import com.rbittencourt.aws.cost.miner.domain.billing.ReservedInstanceInfos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

@Repository
public class ReservedInstanceInfoCsvRepository implements ReservedInstanceInfoRepository {

    @Autowired
    private CsvReader csvReader;

    private String userDir = System.getProperty("user.dir") + "/data.csv";

    @Override
    public ReservedInstanceInfos findReservedInstanceInfos() {
        Predicate<ReservedInstanceInfo> conditionToStop = reserved -> reserved.getUsageType().contains("HeavyUsage");
        List<ReservedInstanceInfo> reservedInstanceInfos = csvReader.csvToObjects(userDir, ReservedInstanceInfo.class, conditionToStop);

        return new ReservedInstanceInfos(reservedInstanceInfos);
    }

}
