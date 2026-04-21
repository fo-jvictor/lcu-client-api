package com.jvictor01.websockets.riot_messaging_service;

import com.jvictor01.riot_client.RiotSignOnService;

import java.net.URI;
import java.util.UUID;

public class RmsService {
    private final RiotSignOnService riotSignOnService;

    public RmsService(RiotSignOnService riotSignOnService) {
        this.riotSignOnService = riotSignOnService;
    }

    public void setupRmsWebsocket() {
        String accessToken = riotSignOnService.getAccessToken();
        String rmsUrl = String.format("wss://us.edge.rms.si.riotgames.com:443/rms/v1/session" +
                        "?token=%s" +
                        "&id=%s" +
                        "&token_type=access" +
                        "&product_id=riot_client" +
                        "&platform=windows" +
                        "&device=desk",
                accessToken, UUID.randomUUID());
        new RmsWebsocketClient(URI.create(rmsUrl), new RmsMessageDecoder()).connect();
    }


}
