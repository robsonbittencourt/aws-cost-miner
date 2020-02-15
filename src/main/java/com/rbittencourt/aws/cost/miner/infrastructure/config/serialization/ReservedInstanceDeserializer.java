package com.rbittencourt.aws.cost.miner.infrastructure.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ReservedInstanceDeserializer extends JsonDeserializer<Boolean> {

    private static final String DBR_RESERVED_YES = "Y";
    private static final String CUR_RESERVED_ARN_SUBSTRING = "reserved-instances";


    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String string = p.readValueAs(String.class);

        if (DBR_RESERVED_YES.equals(string)) {
            return true;
        }

        return StringUtils.isNotBlank(string) && string.contains(CUR_RESERVED_ARN_SUBSTRING);
    }

}
