package com.jvictor01.gameflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

import static com.jvictor01.gameflow.GameflowEndpoints.RECONNECT_URL;
import static com.jvictor01.gameflow.GameflowEndpoints.SESSION_URL;

public class GameflowService {
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;

    public GameflowService() {
        this.httpUtils = new HttpUtils();
        this.objectMapper = new ObjectMapper();
    }

    public String getGameFlowSession() {
        HttpResponse<String> gameFlowSession = httpUtils.buildGetRequest(SESSION_URL);

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


    public void reconnect() {
        HttpResponse<String> response = httpUtils.buildPostRequest(RECONNECT_URL);
    }
}
