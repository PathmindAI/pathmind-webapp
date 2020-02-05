package io.skymind.pathmind.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * A simple object mapper holder for
 * using one single {@link ObjectMapper}
 * across the whole project.
 *
 */
public class ObjectMapperHolder {
    private static ObjectMapper objectMapper = getMapper();

    private ObjectMapperHolder() {}

    /**
     * Get a single object mapper for use
     * with reading and writing json
     * @return
     */
    public static ObjectMapper getJsonMapper() {
        return objectMapper;
    }

    private static ObjectMapper getMapper() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

        return om;
    }
}
