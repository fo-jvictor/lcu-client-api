package com.jvictor01.controllers;

import com.jvictor01.lobby.LobbyService;
import com.jvictor01.matchmaking.MatchmakingService;
import com.jvictor01.utils.ResponseUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class MatchmakingController implements HttpHandler {

    private final LobbyService lobbyService = new LobbyService();
    private final MatchmakingService matchmakingService = new MatchmakingService();
    private final String basePath = "/matchmaking";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

        String subpath = RequestRouteHelper.getSubpathByBaseRoute(basePath, exchange.getRequestURI().getRawPath());

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
        }

        if ("/search".equals(subpath)) {
            System.out.println("vsf: " +subpath);
            HttpResponse<String> response = lobbyService.matchmakingSearch();
            ResponseUtils.send(exchange, response.statusCode(), response.body());
        }

        if ("/cancel".equals(subpath)) {
            System.out.println("cancelando?: " +subpath);
            HttpResponse<String> response = lobbyService.cancelMatchmakingSearch();
            exchange.sendResponseHeaders(response.statusCode(), response.body().length());
        }

        if ("/accept".equalsIgnoreCase(subpath)) {
            HttpResponse<String> stringHttpResponse = matchmakingService.postReadyCheckAccept();
            //TODO: create a service to handle the error response from LCU
            ResponseUtils.send(exchange, stringHttpResponse.statusCode(), stringHttpResponse.body());
        }

        if ("/decline".equalsIgnoreCase(subpath)) {
            HttpResponse<String> stringHttpResponse = matchmakingService.postReadyCheckDecline();
            exchange.sendResponseHeaders(stringHttpResponse.statusCode(), stringHttpResponse.body().length());
        }

    }
}
