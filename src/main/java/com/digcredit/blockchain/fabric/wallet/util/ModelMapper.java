package com.digcredit.blockchain.fabric.wallet.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shniu
 *
 * @link https://www.baeldung.com/jackson-object-mapper-tutorial
 * @link https://www.baeldung.com/category/json/jackson/
 * @link https://www.baeldung.com/jackson-annotations
 * @link https://www.baeldung.com/jackson
 */
public class ModelMapper {
    private static Logger logger = LoggerFactory.getLogger(ModelMapper.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    public Object make(Class<?> targetType, String fromJson) {
        try {
            init();
            return objectMapper.readValue(fromJson, targetType);
        } catch (IOException e) {
            logger.error("JSON转化为对象类型 {} 时出错，json：{}", targetType, fromJson, e);
        }
        return null;
    }

    public List<?> makeList(Class<?> targetType, String fromJson) {
        try {
            init();
            JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, targetType);
            return objectMapper.readValue(fromJson, javaType);
        } catch (IOException e) {
            logger.error("JSON转化为对象类型 List of {} 时出错，json：{}", targetType, fromJson, e);
        }
        return null;
    }

    private void init() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

    public String writeString(Object targetObject) {

        if (targetObject == null) {
            return null;
        }

        String toJson = null;

        try {
            init();
            toJson = objectMapper.writeValueAsString(targetObject);
        } catch (JsonProcessingException e) {
            logger.error("将对象装换成JSON字符串出错", e);
        }

        return toJson;
    }

    public String prettify(Object obj) {
        try {
            init();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // swallow
            logger.warn(e.getMessage());
        }
        return "";
    }
}
