package com.rbittencourt.aws.cost.miner.infrastructure.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String string = p.readValueAs(String.class);

        if (string.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
                        + "[yyyy-MM-dd HH:mm:ss]"
                        + "[yyyy-MM-dd'T'HH:mm:ssX]");

        return LocalDateTime.parse(string, formatter);
    }

}
