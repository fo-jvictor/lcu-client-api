package com.jvictor01.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseUtils {

    public static void send(HttpExchange exchange, int statusCode) {
        send(exchange, statusCode, null);
    }

    public static void send(HttpExchange exchange, int statusCode, String body) {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        OutputStream responseBody = exchange.getResponseBody();
        try {
            if (body != null) {
                exchange.sendResponseHeaders(statusCode, body.getBytes().length);
                responseBody.write(body.getBytes());
            } else {
                exchange.sendResponseHeaders(statusCode, 0);
            }
            responseBody.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
