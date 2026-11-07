package com.jvictor01.chat;

import com.jvictor01.chat.dtos.BotFriendRequest;
import com.jvictor01.chat.dtos.Chat;
import com.jvictor01.chat.dtos.ChatParticipant;
import com.jvictor01.chat.dtos.DeclineBotsResult;
import com.jvictor01.chat.dtos.UpdateChatIcon;
import com.jvictor01.chat.dtos.UpdateRankedData;
import com.jvictor01.chat.dtos.UpdateStatusMessage;
import com.jvictor01.controllers.RequestRouteHelper;
import com.jvictor01.http.Response;
import com.jvictor01.http.ResponseUtils;
import com.jvictor01.utils.JsonBodyParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class ChatController implements HttpHandler {
    private ChatService chatService = new ChatService();
    private final FriendRequestService friendRequestService = new FriendRequestService();
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

        if ("/champ-select-participants".equalsIgnoreCase(subpath)) {
            Response<List<ChatParticipant>> response = chatService.getChampSelectParticipants();
            ResponseUtils.send(exchange, response.getStatusCode(), response.getBody());
            return;
        }

        if ("/friend-requests/bots".equalsIgnoreCase(subpath)) {
            Response<List<BotFriendRequest>> response = friendRequestService.findBotFriendRequests();
            ResponseUtils.send(exchange, response.getStatusCode(), response.getBody());
            return;
        }

        if ("/friend-requests/bots/decline".equalsIgnoreCase(subpath) && "POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Response<DeclineBotsResult> response = friendRequestService.declineBotFriendRequests();
            ResponseUtils.send(exchange, response.getStatusCode(), response.getBody());
            return;
        }

        if ("/update-league-rank".equalsIgnoreCase(subpath)) {
            UpdateRankedData updateRankedData = JsonBodyParser.toDto(exchange, UpdateRankedData::new);
            HttpResponse<String> response = chatService.updateRankedQueueData(updateRankedData);
            ResponseUtils.send(exchange, response.statusCode());
        }

        if ("/icon".equalsIgnoreCase(subpath)) {
            UpdateChatIcon updateChatIcon = JsonBodyParser.toDto(exchange, UpdateChatIcon::new);
            HttpResponse<String> response = chatService.updateChatIcon(updateChatIcon);
            ResponseUtils.send(exchange, response.statusCode(), response.body());
        }

        if ("/status-message".equalsIgnoreCase(subpath)) {
            UpdateStatusMessage updateStatusMessage = JsonBodyParser.toDto(exchange, UpdateStatusMessage::new);
            HttpResponse<String> response = chatService.updateStatusMessage(updateStatusMessage);
            ResponseUtils.send(exchange, response.statusCode(), response.body());
        }
    }
}
