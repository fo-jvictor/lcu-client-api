package com.jvictor01.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.chat.dtos.*;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.http.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChatService {
    private final HttpWebClient httpWebClient;
    private final ObjectMapper objectMapper;

    public ChatService() {
        this.httpWebClient = new HttpWebClient();
        this.objectMapper = new ObjectMapper();
    }

    public Response<List<ChatParticipant>> getChampSelectParticipants() {
        HttpResponse<String> participantsResponse = httpWebClient.buildRequestForRiotClientApi(ChatEndpoints.CHAT_V5_PARTICIPANTS, HttpMethods.GET);
        JSONArray chatParticipantsArray = new JSONObject(participantsResponse.body()).getJSONArray("participants");

        List<ChatParticipant> chatParticipants = new ArrayList<>();
        for (int i = 0; i < chatParticipantsArray.length(); i++) {
            JSONObject jsonObject = chatParticipantsArray.getJSONObject(i);
            ChatParticipant chatParticipant = new ChatParticipant(jsonObject);
            if (chatParticipant.getCid().contains("lol-champ-select")) {
                chatParticipants.add(chatParticipant);
            }
        }

        return new Response<>(200, chatParticipants);
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
