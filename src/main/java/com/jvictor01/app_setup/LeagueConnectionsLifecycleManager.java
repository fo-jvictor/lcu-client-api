package com.jvictor01.app_setup;

import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.websockets.frontend_connection.AvailableWebsocketEvents;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import com.jvictor01.websockets.lcu.LcuWebsocketClient;
import com.jvictor01.websockets.lcu.WebSocketEventListener;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.jvictor01.app_setup.LeagueApisSetup.LCU_AUTHORIZATION_TOKEN;
import static com.jvictor01.app_setup.LeagueApisSetup.LCU_SERVER_PORT;

public class LeagueConnectionsLifecycleManager implements WebSocketEventListener {
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private volatile LcuState lcuState = LcuState.DOWN;
    private LcuState previousState;
    private final HttpWebClient httpWebClient;
    private final FrontendWebsocketConnection frontendWebsocketConnection;
    private final LeagueApisSetup leagueApisSetup = new LeagueApisSetup();
    private LcuWebsocketClient lcuWebsocketClient;

    public LeagueConnectionsLifecycleManager(HttpWebClient httpWebClient, FrontendWebsocketConnection frontendWebsocketConnection) {
        this.httpWebClient = httpWebClient;
        this.frontendWebsocketConnection = frontendWebsocketConnection;
    }

    public synchronized void start() {
        previousState = lcuState;

        service.scheduleWithFixedDelay(() -> {
            try {

                switch (lcuState) {

                    case LcuState.DOWN -> {
                        try {
                            leagueApisSetup.setupLcuAndRiotApis();
                        } catch (Exception e) {
                            break;
                        }

                        if (lcuIsAvailable()) {
                            recreateWebSocketClient();
                            lcuState = LcuState.AVAILABLE;
                        }
                    }

                    case LcuState.AVAILABLE -> {
                        if (!lcuWebsocketClient.isOpen()) {
                            try {
                                lcuWebsocketClient.connect();
                            } catch (Exception e) {
                                lcuState = LcuState.DOWN;
                            }
                        }
                    }

                    case LcuState.WS_CONNECTED -> {
                        if (!lcuWebsocketClient.isOpen()) {
                            lcuState = LcuState.DOWN;
                        }
                    }
                }

                if (lcuState != previousState) {
                    if (lcuState == LcuState.DOWN) {
                        frontendWebsocketConnection.sendMessage(AvailableWebsocketEvents.LCU_CONNECTION_LOST.getValue());
                    } else if (lcuState == LcuState.WS_CONNECTED) {
                        frontendWebsocketConnection.sendMessage(AvailableWebsocketEvents.LCU_CONNECTION_RESTORED.getValue());
                    }

                    previousState = lcuState;
                }

            } catch (Exception e) {
                lcuState = LcuState.DOWN;
                e.printStackTrace();
            }

        }, 0, 2, TimeUnit.SECONDS);
    }

    private boolean lcuIsAvailable() {
        try {
            HttpResponse<String> stringHttpResponse = httpWebClient.buildRequestForLcu("/lol-platform-config/v1/initial-configuration-complete", HttpMethods.GET);
            return stringHttpResponse.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private void recreateWebSocketClient() {
        try {
            if (lcuWebsocketClient != null) {
                lcuWebsocketClient.close();
            }
        } catch (Exception ignored) {}

        try {
            URI uri = new URI("wss://127.0.0.1:" + LCU_SERVER_PORT + "/");
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN);
            lcuWebsocketClient = new LcuWebsocketClient(uri, headers, frontendWebsocketConnection, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLcuWebsocketClient(LcuWebsocketClient lcuWebsocketClient) {
        this.lcuWebsocketClient = lcuWebsocketClient;
    }

    @Override
    public void onClose() {
        this.lcuState = LcuState.DOWN;
    }

    @Override
    public void onOpen() {
        this.lcuState = LcuState.WS_CONNECTED;
    }
}
