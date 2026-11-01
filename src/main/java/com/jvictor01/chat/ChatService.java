package com.jvictor01.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.chat.dtos.Chat;
import com.jvictor01.chat.dtos.UpdateChatIcon;
import com.jvictor01.chat.dtos.UpdateRankedData;
import com.jvictor01.chat.dtos.UpdateStatusMessage;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.http.Response;

import java.net.http.HttpResponse;

public class ChatService {
    private final HttpWebClient httpWebClient;
    private final ObjectMapper objectMapper;

    public ChatService() {
        this.httpWebClient = new HttpWebClient();
        this.objectMapper = new ObjectMapper();
    }

    public HttpResponse<String> updateRankedQueueData(UpdateRankedData updateRankedData) {
        return httpWebClient.buildRequestForLcu(ChatEndpoints.V1_ME, HttpMethods.PUT, updateRankedData);
    }

    public HttpResponse<String> updateChatIcon(UpdateChatIcon updateChatIcon) {
        return httpWebClient.buildRequestForLcu(ChatEndpoints.V1_ME, HttpMethods.PUT, updateChatIcon);
    }

    public HttpResponse<String> updateStatusMessage(UpdateStatusMessage updateStatusMessage) {
        return httpWebClient.buildRequestForLcu(ChatEndpoints.V1_ME, HttpMethods.PUT, updateStatusMessage);
    }

    public Response<Chat> getChatInformation() {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(ChatEndpoints.V1_ME, HttpMethods.GET);
        try {
            Chat chat = objectMapper.readValue(response.body(), Chat.class);
            return new Response<>(response.statusCode(), chat);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
