package com.jvictor01.websockets.lcu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.frontend.FrontendWebsocketServer;
import com.jvictor01.trust_manager.SSLContextFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class LcuWebsocketClient extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FrontendWebsocketServer frontendWebsocketServer;
    private static final String MATCH_FOUND = "Found";

    public LcuWebsocketClient(URI serverUri, Map<String, String> httpHeaders, FrontendWebsocketServer frontendWebsocketServer) {
        super(serverUri, httpHeaders);
        this.frontendWebsocketServer = frontendWebsocketServer;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to LCU WebSocket");
        this.send(LcuWebsocketEvents.LOBBY_INFORMATION);
        this.send(LcuWebsocketEvents.MATCHMAKING_READY_CHECK);
    }

    @Override
    public void onMessage(String message) {
        try {
            Optional.ofNullable(objectMapper.readTree(message))
                    .map(jsonArray -> jsonArray.get(2))
                    .map(eventData -> eventData.get("data"))
                    .map(dataNode -> dataNode.get("searchState"))
                    .map(JsonNode::textValue)
                    .filter(MATCH_FOUND::equalsIgnoreCase)
                    .ifPresent(matchFound -> {
                        frontendWebsocketServer.sendMessage("MATCH FOUND");
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from LCU WebSocket. Code: " + code + ", Reason: " + reason);
        try {
            Thread.sleep(5000);
            this.setSocketFactory(SSLContextFactory.createTrustAllSSLContext().getSocketFactory());
            this.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception ex) {
//        ex.printStackTrace();
    }
}
