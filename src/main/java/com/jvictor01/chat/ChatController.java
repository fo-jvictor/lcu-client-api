package com.jvictor01.chat;

import com.jvictor01.chat.dtos.Chat;
import com.jvictor01.chat.dtos.UpdateRankedData;
import com.jvictor01.controllers.RequestRouteHelper;
import com.jvictor01.http.Response;
import com.jvictor01.http.ResponseUtils;
import com.jvictor01.utils.JsonBodyParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;

public class ChatController implements HttpHandler {
    private ChatService chatService = new ChatService();
    private final String basePath = "/api/chat";

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

        if ("/me".equalsIgnoreCase(subpath)) {
            Response<Chat> chatInformation = chatService.getChatInformation();
            ResponseUtils.send(exchange, chatInformation.getStatusCode(), chatInformation);
            return;
        }

        if ("/update-league-rank".equalsIgnoreCase(subpath)) {
            UpdateRankedData updateRankedData = JsonBodyParser.toDto(exchange, UpdateRankedData::new);
            HttpResponse<String> response = chatService.updateRankedQueueData(updateRankedData);
            ResponseUtils.send(exchange, response.statusCode());
        }
    }
}
