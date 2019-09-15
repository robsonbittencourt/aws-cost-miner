package com.rbittencourt.aws.cost.miner.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    private static final String YES = "Y";
    private static final String NO = "N";

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String string = p.readValueAs(String.class);

        if (YES.equals(string)) {
            return true;
        }

        if (NO.equals(string)) {
            return false;
        }

        if (string.isEmpty()) {
            return false;
        }

        throw new IllegalArgumentException(string + " is not a valid boolean value");
    }

}
