package com.chat.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * Created by lw on 16-12-21.
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper classMapper = new ObjectMapper();
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    static {
        classMapper.enableDefaultTyping();
        classMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        classMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String toJsonString(Object object) {
        String json = "";

        try {
            json = classMapper.writeValueAsString(object);
        } catch (Exception var3) {
            logger.error(object.getClass().getName() + "对象转换包含类型信息的json出错！", var3);
        }

        return json;
    }

    public static Object jsonToObject(String jsonString, Class classType) {
        Object result = null;
        try {
            result = jsonMapper.readValue(jsonString, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
