package com.jvictor01.websockets.lcu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.trust_manager.SSLContextFactory;
import com.jvictor01.websockets.frontend_connection.AvailableWebsocketEvents;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LcuWebsocketClient extends WebSocketClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final FrontendWebsocketConnection frontendWebsocketConnection;
    private final WebSocketEventListener webSocketEventListener;
    private static final String MATCH_FOUND = "Found";
    private boolean readyCheckNotified = false;
    private boolean queueNotified = false;
    private final Map<Long, LobbyMember> currentLobbyMembers = new HashMap<>();

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
            System.out.println("[LCU WS MSG]: " + message);
            JsonNode root = objectMapper.readTree(message);
            JsonNode event = root.get(2);

            if (event == null) {
                return;
            }

            String uri = event.path("uri").asText();
            String eventType = event.path("eventType").asText();
            JsonNode data = event.path("data");
            handleMatchmaking(data);

            if ("/lol-lobby/v2/lobby".equals(uri)) {
                if ("Delete".equalsIgnoreCase(eventType)) {
                    System.out.println("LOBBY DESTROYED");
                    currentLobbyMembers.clear();
                    frontendWebsocketConnection.sendMessage("LOBBY_DESTROYED");
                    return;
                }

                JsonNode members = data.path("members");
                if (members.isArray()) {
                    handleLobbyMembers(members);
                }
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

    private void handleLobbyMembers(JsonNode membersNode) {
        Map<Long, LobbyMember> newMembers = new HashMap<>();

        for (JsonNode memberNode : membersNode) {

            long summonerId = memberNode.path("summonerId").asLong();
            String summonerName = memberNode.path("summonerName").asText();
            String firstRole = memberNode.path("firstPositionPreference").asText();
            String secondRole = memberNode.path("secondPositionPreference").asText();

            LobbyMember member = new LobbyMember(summonerId, summonerName, firstRole, secondRole);
            newMembers.put(summonerId, member);

            if (!currentLobbyMembers.containsKey(summonerId)) {
                System.out.println("PLAYER JOINED: " + summonerName);
                frontendWebsocketConnection.sendMessage("PLAYER_JOINED:" + summonerName);
                continue;
            }

            LobbyMember oldMember = currentLobbyMembers.get(summonerId);

            boolean roleChanged = !Objects.equals(oldMember.getFirstRole(), member.getFirstRole())
                    || !Objects.equals(oldMember.getSecondRole(), member.getSecondRole());

            if (roleChanged) {
                System.out.println("ROLE CHANGED: " + summonerName + " | " + oldMember.getFirstRole() + "/" + oldMember.getSecondRole() + " -> " + member.getFirstRole() + "/" + member.getSecondRole());
                frontendWebsocketConnection.sendMessage("ROLE_CHANGED:" + summonerName);
            }
        }

        for (LobbyMember oldMember : currentLobbyMembers.values()) {
            if (!newMembers.containsKey(oldMember.getSummonerId())) {
                System.out.println("PLAYER LEFT: " + oldMember.getSummonerName());
                frontendWebsocketConnection.sendMessage("PLAYER_LEFT:" + oldMember.getSummonerName());
            }
        }

        currentLobbyMembers.clear();
        currentLobbyMembers.putAll(newMembers);
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
            frontendWebsocketConnection
                    .sendMessage(AvailableWebsocketEvents.SEARCH_STATE_MATCH_FOUND.getValue());
            return;
        }

        boolean readyCheckEnded = isReadyCheckEnded(readyCheckState, playerResponse);

        if (readyCheckEnded && readyCheckNotified) {
            readyCheckNotified = false;
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
