package com.jvictor01.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticFileHandler implements HttpHandler {

    private static final String BASE_PATH = "/web";

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        System.out.println("[STATIC] " + exchange.getRequestURI().getPath());

        if (path.equals("/")) {
            path = "/index.html";
        }

        String resourcePath = BASE_PATH + path;
        InputStream is = getClass().getResourceAsStream(resourcePath);

        if (is == null && !path.contains(".") && !path.startsWith("/assets")) {
            resourcePath = BASE_PATH + "/index.html";
            is = getClass().getResourceAsStream(resourcePath);
        }

        try {
            if (is == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] bytes = is.readAllBytes();

            exchange.getResponseHeaders().set("Content-Type", guessContentType(resourcePath));
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }

        } finally {
            exchange.close();
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}
