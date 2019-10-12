package com.rbittencourt.aws.cost.miner.infrastructure.file;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.rbittencourt.aws.cost.miner.domain.billing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

@Repository
public class BillingInfoCsvRepository implements BillingInfoRepository {

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private CsvSchema csvSchema;

    @Autowired
    private ReservedInstanceInfoRepository reservedInstanceInfoRepository;

    private String userDir = System.getProperty("user.dir") + "/data.csv";

    @Override
    public BillingInfos findBillingInfos() {
        ObjectReader objectReader = csvMapper.readerFor(BillingInfo.class).with(csvSchema);

        List<BillingInfo> billingInfos = new ArrayList<>();

        ReservedInstanceInfos reservedInstanceInfos = reservedInstanceInfoRepository.findReservedInstanceInfos();

        try (Reader reader = new FileReader(userDir, ISO_8859_1)) {
            MappingIterator<BillingInfo> iterator = objectReader.readValues(reader);

            while (iterator.hasNext()) {
                BillingInfo billingInfo = iterator.next();

                if (!billingInfo.getUsageType().contains("HeavyUsage")) {
                    billingInfo.setReservedInstances(reservedInstanceInfos);
                    billingInfos.add(billingInfo);
                }
            }

            return new BillingInfos(billingInfos);
        } catch (IOException e) {
            return new BillingInfos(new ArrayList<>());
        }
    }

}
