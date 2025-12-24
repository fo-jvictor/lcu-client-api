package com.jvictor01.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class JsonBodyParser {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static JSONObject toJson(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            byte[] bytes = is.readAllBytes();
            String body = new String(bytes, StandardCharsets.UTF_8);
            if (body.isBlank()) {
                return new JSONObject();
            }
            return new JSONObject(body);
        }
    }

    public static <T> T toDto(HttpExchange exchange, Function<JSONObject, T> mapper) throws IOException {
        JSONObject json = toJson(exchange);
        return mapper.apply(json);
    }

    public static <T> T toDto(HttpExchange exchange, Class<T> clazz) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            byte[] bytes = is.readAllBytes();
            String body = new String(bytes, StandardCharsets.UTF_8);
            if (body == null || body.isBlank()) {
                return null;
            }
            return MAPPER.readValue(body, clazz);
        }
    }
}
