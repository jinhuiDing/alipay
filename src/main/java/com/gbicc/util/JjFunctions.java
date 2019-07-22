package com.gbicc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2017/10/27.
 */
public class JjFunctions {

    private static ObjectMapper objectMapper = null;

    private static Random random = null;


    public static Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    /**
     * 返回单例 ObjectMapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                    jsonGenerator.writeString("");
                }
            });
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(format);
        }
        return objectMapper;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
