package com.jvictor01.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void send(HttpExchange exchange, int statusCode) {
        send(exchange, statusCode, (Object) null);
    }

    public static void send(HttpExchange exchange, int statusCode, String body) {
        send(exchange, statusCode, (Object) body);
    }

    public static void send(HttpExchange exchange, int statusCode, Object body) {
        try (OutputStream responseBody = exchange.getResponseBody()) {
            if (body != null) {
                String json;
                if (body instanceof String) {
                    json = (String) body;
                } else {
                    json = MAPPER.writeValueAsString(body);
                }
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(statusCode, bytes.length);
                responseBody.write(bytes);
            } else {
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(statusCode, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
