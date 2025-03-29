package com.jvictor01.controllers;

import com.jvictor01.lobby.LobbyService;
import com.jvictor01.matchmaking.MatchmakingService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class MatchmakingController implements HttpHandler {

    private final LobbyService lobbyService;
    private final MatchmakingService matchmakingService;

    public MatchmakingController() {
        this.lobbyService = new LobbyService();
        this.matchmakingService = new MatchmakingService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

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

        if ("/matchmaking/accept".equalsIgnoreCase(exchange.getRequestURI().toString())) {
            HttpResponse<String> stringHttpResponse = matchmakingService.postReadyCheckAccept();
            //TODO: create a service to handle the error response from LCU
            exchange.sendResponseHeaders(stringHttpResponse.statusCode(), stringHttpResponse.body().length());
        }

        if ("/matchmaking/decline".equalsIgnoreCase(exchange.getRequestURI().toString())) {
            HttpResponse<String> stringHttpResponse = matchmakingService.postReadyCheckDecline();
            exchange.sendResponseHeaders(stringHttpResponse.statusCode(), stringHttpResponse.body().length());
        }

    }
}
