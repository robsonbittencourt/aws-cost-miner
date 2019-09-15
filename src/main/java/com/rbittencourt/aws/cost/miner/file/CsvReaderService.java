package com.rbittencourt.aws.cost.miner.file;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private CsvSchema csvSchema;

    public <T> List<T> csvToObjects(String filePath, Class<T> clazz) {
        ObjectReader objectReader = csvMapper.readerFor(clazz).with(csvSchema);

        List<T> objects = new ArrayList<>();

        try (Reader reader = new FileReader(filePath, StandardCharsets.ISO_8859_1)) {
            MappingIterator<T> iterator = objectReader.readValues(reader);

            while (iterator.hasNext()) {
                objects.add(iterator.next());
            }

            return objects;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
