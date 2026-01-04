package com.jvictor01.websockets.lcu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.trust_manager.SSLContextFactory;
import com.jvictor01.websockets.frontend_connection.AvailableWebsocketEvents;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class LcuWebsocketClient extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FrontendWebsocketConnection frontendWebsocketConnection;
    private final WebSocketEventListener webSocketEventListener;
    private static final String MATCH_FOUND = "Found";
    private boolean readyCheckNotified = false;
    private boolean queueNotified = false;

    public LcuWebsocketClient(URI serverUri, Map<String, String> httpHeaders,
                              FrontendWebsocketConnection frontendWebsocketConnection,
                              WebSocketEventListener webSocketEventListener) {
        super(serverUri, httpHeaders);
        setSocketFactory(SSLContextFactory.createTrustAllSSLContext().getSocketFactory());
        this.frontendWebsocketConnection = frontendWebsocketConnection;
        this.webSocketEventListener = webSocketEventListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to LCU WebSocket");
        this.send(LcuWebsocketEvents.LOBBY_INFORMATION);
        this.send(LcuWebsocketEvents.MATCHMAKING_READY_CHECK);
        this.webSocketEventListener.onOpen();
    }

    @Override
    public void onMessage(String message) {
        try {
            Optional.ofNullable(objectMapper.readTree(message))
                    .map(jsonArray -> jsonArray.get(2))
                    .map(eventData -> eventData.get("data"))
                    .ifPresent(data -> {

                        String searchState = data.path("searchState").asText(null);
                        JsonNode readyCheck = data.path("readyCheck");
                        String readyCheckState = readyCheck.path("state").asText(null);
                        String playerResponse = readyCheck.path("playerResponse").asText(null);

                        boolean readyCheckActive = isReadyCheckActive(searchState, readyCheckState, playerResponse);
                        boolean inQueue = isIsCurrentlyInQueue(data, searchState);

                        if (inQueue && !queueNotified) {
                            frontendWebsocketConnection.sendMessage(AvailableWebsocketEvents.SEARCH_STATE_QUEUE_STARTED.getValue());
                            queueNotified = true;
                        }

                        if (!inQueue) {
                            frontendWebsocketConnection.sendMessage(AvailableWebsocketEvents.SEARCH_STATE_QUEUE_ENDED.getValue());
                            queueNotified = false;
                        }

                        if (readyCheckActive && !readyCheckNotified) {
                            readyCheckNotified = true;
                            frontendWebsocketConnection.sendMessage(AvailableWebsocketEvents.SEARCH_STATE_MATCH_FOUND.getValue());
                            return;
                        }

                        boolean readyCheckEnded = isReadyCheckEnded(readyCheckState, playerResponse);

                        if (readyCheckEnded && readyCheckNotified) {
                            readyCheckNotified = false;
                        }
                    });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from LCU WebSocket. Code: " + code + ", Reason: " + reason);
        this.webSocketEventListener.onClose();
    }

    @Override
    public void onError(Exception ex) {
//        ex.printStackTrace();
    }

    private boolean isReadyCheckActive(String searchState, String readyCheckState, String playerResponse) {
        return MATCH_FOUND.equalsIgnoreCase(searchState)
                && "InProgress".equalsIgnoreCase(readyCheckState)
                && "None".equalsIgnoreCase(playerResponse);
    }

    private boolean isReadyCheckEnded(String readyCheckState, String playerResponse) {
        return !"InProgress".equalsIgnoreCase(readyCheckState)
                || !"None".equalsIgnoreCase(playerResponse);
    }

    private boolean isIsCurrentlyInQueue(JsonNode data, String searchState) {
        return "Searching".equalsIgnoreCase(searchState)
                && data.path("isCurrentlyInQueue").asBoolean();
    }
}
