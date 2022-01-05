package pers.lurker.webconfig.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper objectMapper;

    public JsonUtil() {}

    @Autowired
    public JsonUtil(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    public static String obj2String(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T string2Obj(String string, Class<T> tClass) {
        try {
            return objectMapper.readValue(string, tClass);
        } catch (IOException e) {
            log.error("source:" + string + "\n" + e.getMessage(), e);
        }
        return null;
    }

    public static <T> T bytes2Obj(byte[] bytes, Class<T> tClass) {
        try {
            return objectMapper.readValue(bytes, tClass);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
