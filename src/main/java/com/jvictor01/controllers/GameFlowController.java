package com.jvictor01.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.gameflow.GameflowService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class GameFlowController implements HttpHandler {

    private final GameflowService gameflowService;
    private final ObjectMapper objectMapper;

    public GameFlowController() {
        this.gameflowService = new GameflowService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

        if ("/gameflow/session".equalsIgnoreCase(exchange.getRequestURI().toString())) {
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
