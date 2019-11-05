package com.rbittencourt.aws.cost.miner.infrastructure.file;

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
import java.util.function.Predicate;

@Service
public class CsvReader {

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private CsvSchema csvSchema;

    public <T> List<T> csvToObjects(String filePath, Class<T> clazz) {
        return csvToObjects(filePath, clazz, t -> true);
    }

    public <T> List<T> csvToObjects(String filePath, Class<T> clazz, Predicate<T> stopCondition) {
        ObjectReader objectReader = csvMapper.readerFor(clazz).with(csvSchema);

        List<T> objects = new ArrayList<>();

        try (Reader reader = new FileReader(filePath, StandardCharsets.ISO_8859_1)) {
            MappingIterator<T> iterator = objectReader.readValues(reader);

            while (iterator.hasNext()) {
                T next = iterator.next();

                if (stopCondition.test(next)) {
                    objects.add(next);
                }
            }

            return objects;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
