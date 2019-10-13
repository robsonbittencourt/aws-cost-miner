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
public class ReservedInstanceInfoCsvRepository implements ReservedInstanceInfoRepository {

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private CsvSchema csvSchema;

    private String userDir = System.getProperty("user.dir") + "/data.csv";

    @Override
    public ReservedInstanceInfos findReservedInstanceInfos() {
        ObjectReader objectReader = csvMapper.readerFor(ReservedInstanceInfo.class).with(csvSchema);

        List<ReservedInstanceInfo> reservedInstanceInfos = new ArrayList<>();

        try (Reader reader = new FileReader(userDir, ISO_8859_1)) {
            MappingIterator<ReservedInstanceInfo> iterator = objectReader.readValues(reader);

            while (iterator.hasNext()) {
                ReservedInstanceInfo info = iterator.next();

                if (info.getUsageType().contains("HeavyUsage")) {
                    reservedInstanceInfos.add(info);
                } else {
                    break;
                }
            }

            return new ReservedInstanceInfos(reservedInstanceInfos);
        } catch (IOException e) {
            return new ReservedInstanceInfos(new ArrayList<>());
        }
    }

}
