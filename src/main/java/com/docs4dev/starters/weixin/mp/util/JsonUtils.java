package com.docs4dev.starters.weixin.mp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T parse(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid json, json: " + json);
        }
    }

    public static <T> T parse(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new IllegalArgumentException("invalid json, json: " + json);
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean hasNode(String json, String nodeName) {
        try {
            JsonNode node = OBJECT_MAPPER.readTree(json);
            return node != null && node.get(nodeName) != null;
        } catch (IOException e) {
            return false;
        }
    }

    public static <T> T readNode(String src, String nodeName, Class<T> clazz) {
        try {
            JsonNode node = OBJECT_MAPPER.readTree(src);
            if (node == null || node.get(nodeName) == null) {
                return null;
            }
            String json = node.get(nodeName).asText();
            return parse(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
