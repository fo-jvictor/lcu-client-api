package com.jvictor01.gameflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;

import java.net.http.HttpResponse;

public class GameflowService {
    private final HttpWebClient httpWebClient;
    private final ObjectMapper objectMapper;

    public GameflowService() {
        this.httpWebClient = new HttpWebClient();
        this.objectMapper = new ObjectMapper();
    }

    public String getGameFlowSession() {
        HttpResponse<String> gameFlowSession = httpWebClient.buildRequestForLcu(GameflowEndpoints.SESSION_URL, HttpMethods.GET);

        if (gameFlowSession.statusCode() != 200) {
            return GameflowStateEnum.NONE.getState();
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(gameFlowSession.body());
            String phase = jsonNode.get("phase").asText();
            return GameflowStateEnum.getFromString(phase).getState();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON response", e);
        }
    }

}
