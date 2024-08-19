package com.jvictor01.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class WebsocketAuthentication extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MATCH_FOUND = "Found";

    public WebsocketAuthentication(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to LCU WebSocket");
        String matchmakingReadyCheckEvent = """
                [5, "OnJsonApiEvent_lol-matchmaking_v1_search"]
                """;
        this.send(matchmakingReadyCheckEvent);
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode jsonArray = objectMapper.readTree(message);
            JsonNode eventData = jsonArray.get(2);

            var data = eventData.get("data");
            var searchState = data.get("searchState");

            if (data != null && searchState != null && MATCH_FOUND.equalsIgnoreCase(searchState.textValue())) {
                System.out.println("Match found?");
            }

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
