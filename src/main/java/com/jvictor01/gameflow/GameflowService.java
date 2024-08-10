package com.jvictor01.gameflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

import static com.jvictor01.gameflow.GameflowEndpoints.RECONNECT_URL;
import static com.jvictor01.gameflow.GameflowEndpoints.SESSION_URL;

public class GameflowService {
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;
    private final SummonerService summonerService;

    public GameflowService() {
        this.objectMapper = new ObjectMapper();
        this.httpUtils = new HttpUtils();
        this.summonerService = new SummonerService();
    }

    public void getGameFlowSession() {
        HttpResponse<String> response = httpUtils.buildGetRequest(SESSION_URL);
        System.out.println(response.body());
    }

    public void reconnect() {
        HttpResponse<String> response = httpUtils.buildPostRequest(RECONNECT_URL);
        System.out.println(response.body());
    }
}
