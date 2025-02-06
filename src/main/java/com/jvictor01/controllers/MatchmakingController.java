package com.jvictor01.controllers;

import com.jvictor01.lobby.LobbyService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class MatchmakingController implements HttpHandler {

    private final LobbyService lobbyService;

    public MatchmakingController() {
        this.lobbyService = new LobbyService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE");

        if ("/search-matchmaking".equals(exchange.getRequestURI().toString())) {
            HttpResponse<String> response = lobbyService.matchmakingSearch();
            exchange.sendResponseHeaders(response.statusCode(), response.body().length());
        }

        if ("/cancel-search-matchmaking".equalsIgnoreCase(exchange.getRequestURI().toString())) {
            HttpResponse<String> response = lobbyService.cancelMatchmakingSearch();
            System.out.println(response);
            System.out.println("body: " + response.body());
            exchange.sendResponseHeaders(response.statusCode(), response.body().length());
        }

    }
}
