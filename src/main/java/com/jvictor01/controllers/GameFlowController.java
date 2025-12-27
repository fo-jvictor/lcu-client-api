package com.jvictor01.controllers;

import com.jvictor01.gameflow.GameflowService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class GameFlowController implements HttpHandler {
    private final GameflowService gameflowService = new GameflowService();
    private final String basePath = "/api/gameflow";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

        String subpath = RequestRouteHelper.getSubpathByBaseRoute(basePath, exchange.getRequestURI().getRawPath());

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("/session".equalsIgnoreCase(subpath)) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String gameFlowSession = gameflowService.getGameFlowSession();
            String jsonResponse = "{\"phase\": \"" + gameFlowSession + "\"}";
            byte[] responseBytes = jsonResponse.getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();
        }
    }

}
