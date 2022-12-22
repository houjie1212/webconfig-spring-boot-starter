package pers.lurker.webconfig.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.lurker.webconfig.exception.ext.JsonParseException;

import java.io.IOException;

@Component
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper;

    public JsonUtil() {}

    @Autowired
    public JsonUtil(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
        JsonUtil.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String obj2String(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonParseException(object, e);
        }
    }

    public static <T> T string2Obj(String string, Class<T> tClass) {
        try {
            return objectMapper.readValue(string, tClass);
        } catch (IOException e) {
            throw new JsonParseException(string, e);
        }
    }

    public static <T> T bytes2Obj(byte[] bytes, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (IOException e) {
            throw new JsonParseException(bytes, e);
        }
    }
}
