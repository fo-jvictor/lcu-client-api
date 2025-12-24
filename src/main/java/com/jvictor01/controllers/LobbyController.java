package com.jvictor01.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.lobby.Lobby;
import com.jvictor01.lobby.LobbyRoles;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.lobby.LobbySettings;
import com.jvictor01.utils.JsonBodyParser;
import com.jvictor01.utils.ResponseUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyController implements HttpHandler {

    private final LobbyService lobbyService = new LobbyService();
    private final String basePath = "/lobby";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");

        String subpath = RequestRouteHelper.getSubpathByBaseRoute(basePath, exchange.getRequestURI().getRawPath());

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
            exchange.sendResponseHeaders(204, -1);
        }

        if ("/queue-ids".equals(subpath)) {
            HttpResponse<String> response = lobbyService.getLobbys();
            List<Lobby> lobbies = objectMapper.readValue(response.body(), new TypeReference<List<Lobby>>() {
            });

            String jsonResponse = objectMapper.writeValueAsString(lobbies);
            byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(bytes);
            responseBody.close();
        }

        if ("/create".equals(subpath)) {
            LobbySettings lobbySettings = JsonBodyParser.toDto(exchange, LobbySettings::new);
            HttpResponse<String> response = lobbyService.createLobby(lobbySettings);
            ResponseUtils.send(exchange, response.statusCode());
        }

        if ("/invite-custom-summoners".contains(subpath)) {
            lobbyService.postCustomInvitation();
        }

        if ("/update-positions-preference".contains(subpath)) {
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
