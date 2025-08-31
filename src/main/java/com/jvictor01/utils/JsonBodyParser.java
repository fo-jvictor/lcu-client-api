package com.jvictor01.utils;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class JsonBodyParser {

    public static JSONObject toJson(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            byte[] bytes = is.readAllBytes();
            return new JSONObject(new String(bytes));
        }
    }

    public static <T> T toDto(HttpExchange exchange, Function<JSONObject, T> mapper) throws IOException {
        JSONObject json = toJson(exchange);
        return mapper.apply(json);
    }
}
