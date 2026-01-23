package com.jvictor01.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.http.Response;
import com.jvictor01.http.ResponseUtils;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.lobby.dtos.GameQueue;
import com.jvictor01.lobby.dtos.LobbyRoles;
import com.jvictor01.lobby.dtos.LobbySettings;
import com.jvictor01.utils.JsonBodyParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyController implements HttpHandler {

    private final LobbyService lobbyService = new LobbyService();
    private final String basePath = "/api/lobby";
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        if ("/create".equals(subpath)) {
            LobbySettings lobbySettings = JsonBodyParser.toDto(exchange, LobbySettings::new);
            HttpResponse<String> response = lobbyService.createLobby(lobbySettings);
            ResponseUtils.send(exchange, response.statusCode());
            return;
        }

        if ("/available-queues".equals(subpath)) {
            Response<List<GameQueue>> availableGameQueues = lobbyService.getAvailableGameQueues();
            ResponseUtils.send(exchange, availableGameQueues.getStatusCode(), availableGameQueues.getBody());
        }

        if (subpath.contains("/update-positions-preference")) {
            LobbyRoles lobbyRoles = JsonBodyParser.toDto(exchange, LobbyRoles::new);
            HttpResponse<String> response = lobbyService.updatePositionPreferences(lobbyRoles);
            ResponseUtils.send(exchange, response.statusCode());
        }

        if ("/invite-summoner".contains(subpath)) {
            String rawQuery = exchange.getRequestURI().getRawQuery();
            Map<String, String> nicknameAndTagline = getNicknameAndTagLineFromParams(rawQuery);
            String nickname = nicknameAndTagline.get("nickname");
            String tag = nicknameAndTagline.get("tagLine");
            HttpResponse<String> response = lobbyService.postInvitationByNickname(nickname, tag);
            ResponseUtils.send(exchange, response.statusCode());
            return;
        }

        if ("/remove-summoner".contains(subpath)) {
            String rawQuery = exchange.getRequestURI().getRawQuery();
            String[] splittedUri = rawQuery.split("=");
            String summonerId = splittedUri[1];
            HttpResponse<String> response = lobbyService.kickSummonerBySummonerId(summonerId);
            ResponseUtils.send(exchange, response.statusCode());
        }

    }

    private Map<String, String> getNicknameAndTagLineFromParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query == null) {
            throw new RuntimeException("query must not be empty");
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(
                        URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)
                );
            }
        }

        return queryParams;
    }

}
