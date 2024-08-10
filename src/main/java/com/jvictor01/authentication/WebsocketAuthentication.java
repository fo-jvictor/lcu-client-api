package com.jvictor01.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.matchmaking.MatchmakingService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.Scanner;

public class WebsocketAuthentication extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MatchmakingService matchmakingService = new MatchmakingService();

    public WebsocketAuthentication(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to LCU WebSocket");
        String matchmakingReadyCheckEvent = """
                [5, "OnJsonApiEvent_lol-matchmaking_v1_ready-check"]
                """;
        this.send(matchmakingReadyCheckEvent);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            JsonNode jsonArray = objectMapper.readTree(message);
            JsonNode eventData = jsonArray.get(2);
            System.out.println("Event Data: " + eventData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from LCU WebSocket. Code: " + code + ", Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
//        ex.printStackTrace();
    }
}
