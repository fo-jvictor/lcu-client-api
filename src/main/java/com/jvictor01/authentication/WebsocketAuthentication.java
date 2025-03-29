package com.jvictor01.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.frontend.FrontendWebsocketServer;
import com.jvictor01.lcu_web_socket.LcuWebsocketEvents;
import com.jvictor01.trust_manager.SSLContextFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class WebsocketAuthentication extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FrontendWebsocketServer frontendWebsocketServer;
    private static final String MATCH_FOUND = "Found";

    public WebsocketAuthentication(URI serverUri, Map<String, String> httpHeaders, FrontendWebsocketServer frontendWebsocketServer) {
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
            JsonNode jsonArray = objectMapper.readTree(message);
            JsonNode eventData = jsonArray.get(2);

            var data = eventData.get("data");
            var searchState = data.get("searchState");


            if (searchState != null && MATCH_FOUND.equalsIgnoreCase(searchState.textValue())) {
                frontendWebsocketServer.sendMessage("MATCH FOUND");
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
