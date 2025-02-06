package com.jvictor01.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.lobby.LobbyEndpoints;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.matchmaking.MatchmakingEndpoints;
import com.jvictor01.matchmaking.MatchmakingService;
import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Controller implements HttpHandler {
    private final MatchmakingService matchmakingService;
    private final LobbyService lobbyService;
    private final SummonerService summonerService;
    private final ObjectMapper objectMapper;

    public Controller() {
        this.matchmakingService = new MatchmakingService();
        this.lobbyService = new LobbyService();
        this.summonerService = new SummonerService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");


        if (exchange.getRequestURI().toString().equals("/custom-url")) {
            Summoner[] summoners = summonerService.getSummonerDetailsByNickname("fortune tiger#KTO");
            Summoner summoner = summoners[0];
            String jsonResponse = objectMapper.writeValueAsString(summoner);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            if (LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH.contains(exchange.getRequestURI().toString())) {
                //lobbyService.queue();
                exchange.sendResponseHeaders(200, "teste".getBytes().length);
            }


            if (MatchmakingEndpoints.READY_CHECK_ACCEPT.contains(exchange.getRequestURI().toString())) {
                matchmakingService.postReadyCheckAccept();
                exchange.sendResponseHeaders(200, "teste".getBytes().length);

                if (MatchmakingEndpoints.READY_CHECK_DECLINE.contains(exchange.getRequestURI().toString())) {
                    matchmakingService.postReadyCheckDecline();
                    exchange.sendResponseHeaders(200, "teste".getBytes().length);
                }
            }
        }
    }

    private void getRequestInformation(HttpExchange exchange) {
        System.out.println(exchange);
    }
}
