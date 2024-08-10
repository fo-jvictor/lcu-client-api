package com.jvictor01.lobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

public class LobbyService {
    private final HttpUtils httpUtils;

    public LobbyService() {
        this.httpUtils = new HttpUtils();
    }

    public void queue() {
        System.out.println("IN QUEUE:");
        httpUtils.buildPostRequest(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH);
    }

    public void playAgain() {
        httpUtils.buildPostRequest(LobbyEndpoints.PLAY_AGAIN);
    }
}
