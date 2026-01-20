package com.jvictor01.utils;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class JsonBodyParser {

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

}
