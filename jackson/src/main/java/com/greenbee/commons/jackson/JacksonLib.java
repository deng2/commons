package com.greenbee.commons.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class JacksonLib {

    private static ObjectMapper om_ = null;

    public static synchronized ObjectMapper getObjectMapper() {
        if (om_ == null) {
            ObjectMapper om = newObjectMapper();
            om_ = om;
        }
        return om_;
    }

    private static ObjectMapper newObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return om;
    }

    public static <T> T readValue(String json, Class<T> type)
            throws JsonMappingException, JsonParseException, IOException {
        return getObjectMapper().readValue(json, type);
    }

    public static String write(Object obj)
            throws IOException, JsonGenerationException, JsonMappingException {
        StringWriter sw = new StringWriter();
        getObjectMapper().writeValue(sw, obj);
        return sw.toString();
    }
}
