package com.jvictor01.websockets.lcu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.gameflow.GameflowStateEnum;
import com.jvictor01.matchmaking.AutoAcceptSettings;
import com.jvictor01.matchmaking.MatchmakingService;
import com.jvictor01.utils.trust_manager.SSLContextFactory;
import com.jvictor01.websockets.frontend_connection.AvailableWebsocketEvents;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class LcuWebsocketClient extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FrontendWebsocketConnection frontendWebsocketConnection;
    private final WebSocketEventListener webSocketEventListener;
    private static final String MATCH_FOUND = "Found";
    private boolean readyCheckNotified = false;
    private boolean queueNotified = false;
    private boolean champSelectNotified = false;
    private final MatchmakingService matchmakingService = new MatchmakingService();

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
        this.send(LcuWebsocketEvents.GAMEFLOW_PHASE);
        this.webSocketEventListener.onOpen();
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode event = root.get(2);

            if (event == null) {
                return;
            }

            String uri = event.path("uri").asText();
            String eventType = event.path("eventType").asText();
            JsonNode data = event.path("data");

            if (uri.contains("/lol-matchmaking")) {
                handleMatchmaking(data);
            }

            if ("/lol-gameflow/v1/gameflow-phase".equals(uri)) {
                handleGameflowPhase(data.asText());
                return;
            }

            if ("/lol-lobby/v2/lobby".equals(uri) && "Delete".equalsIgnoreCase(eventType)) {
                frontendWebsocketConnection.sendMessage("LOBBY_DESTROYED");
            }

        } catch (Exception e) {
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

    private void handleMatchmaking(JsonNode data) {
        String searchState = data.path("searchState").asText(null);
        JsonNode readyCheck = data.path("readyCheck");
        String readyCheckState = readyCheck.path("state").asText(null);
        String playerResponse = readyCheck.path("playerResponse").asText(null);
        boolean readyCheckActive = isReadyCheckActive(searchState, readyCheckState, playerResponse);
        boolean inQueue = isCurrentlyInQueue(data, searchState);

        if (inQueue && !queueNotified) {
            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.SEARCH_STATE_QUEUE_STARTED.getValue());
            queueNotified = true;
        }

        if (!inQueue) {
            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.SEARCH_STATE_QUEUE_ENDED.getValue());
            queueNotified = false;
        }

        if (readyCheckActive && !readyCheckNotified) {
            readyCheckNotified = true;

            if (AutoAcceptSettings.isEnabled() && autoAcceptReadyCheck()) {
                frontendWebsocketConnection
                        .sendMessage(AvailableWebsocketEvents.SEARCH_STATE_MATCH_AUTO_ACCEPTED.getValue());
                return;
            }

            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.SEARCH_STATE_MATCH_FOUND.getValue());
            return;
        }

        boolean readyCheckEnded = isReadyCheckEnded(readyCheckState, playerResponse);

        if (readyCheckEnded && readyCheckNotified) {
            readyCheckNotified = false;
        }
    }

    private void handleGameflowPhase(String phase) {
        boolean inChampSelect = GameflowStateEnum.CHAMP_SELECT.getState().equalsIgnoreCase(phase);

        if (inChampSelect && !champSelectNotified) {
            champSelectNotified = true;
            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.CHAMP_SELECT_STARTED.getValue());
            return;
        }

        if (!inChampSelect && champSelectNotified) {
            champSelectNotified = false;
            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.CHAMP_SELECT_ENDED.getValue());
        }
    }

    private boolean autoAcceptReadyCheck() {
        try {
            int statusCode = matchmakingService.postReadyCheckAccept().statusCode();
            return statusCode >= 200 && statusCode < 300;
        } catch (Exception e) {
            return false;
        }
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

    private boolean isCurrentlyInQueue(JsonNode data, String searchState) {
        return "Searching".equalsIgnoreCase(searchState)
                && data.path("isCurrentlyInQueue").asBoolean();
    }
}
