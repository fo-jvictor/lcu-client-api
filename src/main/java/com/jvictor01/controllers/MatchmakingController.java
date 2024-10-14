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
        String requestMethod = exchange.getRequestMethod();

        if ("/search-matchmaking".equals(exchange.getRequestURI().toString())) {

            if ("Delete".equalsIgnoreCase(requestMethod)) {
                HttpResponse<String> stringHttpResponse = lobbyService.cancelMatchmakingSearch();
                exchange.sendResponseHeaders(stringHttpResponse.statusCode(), stringHttpResponse.body().length());
            }

            lobbyService.matchmakingSearch();
            exchange.sendResponseHeaders(200, 0);
        }


    }
}
