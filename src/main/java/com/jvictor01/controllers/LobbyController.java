package com.jvictor01.controllers;

import com.jvictor01.lobby.LobbyRoles;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.lobby.LobbySettings;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LobbyController implements HttpHandler {

    private final LobbyService lobbyService;

    public LobbyController() {
        this.lobbyService = new LobbyService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if ("/create-lobby".equals(exchange.getRequestURI().toString())) {
            InputStream requestBody = exchange.getRequestBody();
            byte[] bytes = requestBody.readAllBytes();
            JSONObject jsonObject = new JSONObject(new String(bytes));
            LobbySettings settings = new LobbySettings(jsonObject);
            lobbyService.createLobby(settings);
            exchange.sendResponseHeaders(200, 0);
        }

        if ("/update-positions-preference".contains(exchange.getRequestURI().getPath())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            InputStream requestBody = exchange.getRequestBody();
            byte[] bytes = requestBody.readAllBytes();
            JSONObject jsonObject = new JSONObject(new String(bytes));
            LobbyRoles lobbyRoles = new LobbyRoles(jsonObject);
            lobbyService.updatePositionPreferences(lobbyRoles);
        }

        if ("/invite-summoner".contains(exchange.getRequestURI().getPath())) {
            String rawQuery = exchange.getRequestURI().getRawQuery();
            Map<String, String> nicknameAndTagline = getNicknameAndTagLineFromParams(rawQuery);
            String nickname = nicknameAndTagline.get("nickname");
            String tag = nicknameAndTagline.get("tagLine");
            HttpResponse<String> response = lobbyService.postInvitationByNickname(nickname, tag);
            exchange.sendResponseHeaders(response.statusCode(), response.body().length());
        }

        if ("/remove-summoner".contains(exchange.getRequestURI().getPath())) {
            String rawQuery = exchange.getRequestURI().getRawQuery();
            String[] splittedUri = rawQuery.split("=");
            String summonerId = splittedUri[1];
            HttpResponse<String> response = lobbyService.kickSummonerBySummonerId(summonerId);
            exchange.sendResponseHeaders(response.statusCode(), response.body().length());
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
