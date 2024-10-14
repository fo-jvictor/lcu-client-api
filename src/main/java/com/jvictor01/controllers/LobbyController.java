package com.jvictor01.controllers;

import com.jvictor01.lobby.LobbyRoles;
import com.jvictor01.lobby.LobbyService;
import com.jvictor01.lobby.LobbySettings;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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

        if ("/update-positions-preference".contains(exchange.getRequestURI().toString())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            InputStream requestBody = exchange.getRequestBody();
            byte[] bytes = requestBody.readAllBytes();
            JSONObject jsonObject = new JSONObject(new String(bytes));

            LobbyRoles lobbyRoles = new LobbyRoles(jsonObject);

            lobbyService.updatePositionPreferences(lobbyRoles);
        }


    }
}
