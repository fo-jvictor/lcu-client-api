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
import java.util.function.Predicate;

public class ChatService {
    private final HttpWebClient httpWebClient;
    private final ObjectMapper objectMapper;

    public ChatService() {
        this.httpWebClient = new HttpWebClient();
        this.objectMapper = new ObjectMapper();
    }

    public Response<List<ChatParticipant>> getChampSelectParticipants() {
//        HttpResponse<String> session = httpWebClient.buildRequestForLcu(LobbyTeamBuilderEndpoints.CHAMP_SELECT_SESSION, HttpMethods.GET);
//        if (session.statusCode() < 200 || session.statusCode() >= 300) {
//            return new Response<>(session.statusCode(), List.of());
//        }
//
//        JSONObject jsonObject = new JSONObject(session.body());
//        Optional<String> multiUserChatId = Optional.ofNullable(jsonObject)
//                .map(ChatDetails::new)
//                .map(ChatDetails::getMultiUserChatId);
//
//        String myPuuid = getChatInformation().getBody().getPuuid();
//
//        HttpResponse<String> response = httpWebClient.buildRequestForRiotClientApi(ChatEndpoints.CHAT_V5_PARTICIPANTS, HttpMethods.GET);
//        List<ChatParticipant> participants = new JSONObject(response.body()).getJSONArray("participants");
//
//        List<JSONObject> champSelectRoom = filterByCid(participants, cid -> !multiUserChatId.isEmpty() && cid.startsWith(multiUserChatId.get() + "@"));
//        if (champSelectRoom.isEmpty()) {
//            champSelectRoom = filterByCid(participants, cid -> cid.contains("champ-select."));
//        }
//
//        Map<String, ChatParticipant> byPuuid = new LinkedHashMap<>();
//        for (JSONObject participant : champSelectRoom) {
//            String puuid = participant.optString("puuid");
//            if (!puuid.isEmpty() && !puuid.equals(myPuuid)) {
//                byPuuid.putIfAbsent(puuid, new ChatParticipant(participant));
//            }
//        }

        return new Response<>(200, new ArrayList<>());
    }

    private List<JSONObject> filterByCid(JSONArray participants, Predicate<String> cidMatches) {
        List<JSONObject> filtered = new ArrayList<>();
        for (int i = 0; i < participants.length(); i++) {
            JSONObject participant = participants.getJSONObject(i);
            if (cidMatches.test(participant.optString("cid"))) {
                filtered.add(participant);
            }
        }
        return filtered;
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
