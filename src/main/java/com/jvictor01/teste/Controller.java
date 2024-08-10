package com.jvictor01.teste;

import com.jvictor01.lobby.LobbyEndpoints;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.matchmaking.MatchmakingEndpoints;
import com.jvictor01.matchmaking.MatchmakingService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class Controller implements HttpHandler {
    private final MatchmakingService matchmakingService;
    private final LobbyService lobbyService;

    public Controller() {
        this.matchmakingService = new MatchmakingService();
        this.lobbyService = new LobbyService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if ("POST".equals(exchange.getRequestMethod())) {
            if (LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH.contains(exchange.getRequestURI().toString())) {
                lobbyService.queue();
                exchange.sendResponseHeaders(200, "teste".getBytes().length);
            }

            if (LobbyEndpoints.PLAY_AGAIN.contains(exchange.getRequestURI().toString())) {
                lobbyService.playAgain();
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
