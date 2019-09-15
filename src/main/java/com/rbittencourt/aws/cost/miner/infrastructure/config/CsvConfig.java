package com.rbittencourt.aws.cost.miner.infrastructure.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE;

@Configuration
public class CsvConfig {

    private static final char DOUBLE_QUOTE = '"';

    @Bean
    public CsvMapper csvMapper() {
        CsvMapper csvMapper = new CsvMapper();

        csvMapper.findAndRegisterModules();
        csvMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        csvMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        csvMapper.enable(IGNORE_TRAILING_UNMAPPABLE);

        return csvMapper;
    }

    @Bean
    public CsvSchema csvSchema() {
        return CsvSchema.emptySchema()
                .withHeader()
                .withQuoteChar(DOUBLE_QUOTE);
    }

}
