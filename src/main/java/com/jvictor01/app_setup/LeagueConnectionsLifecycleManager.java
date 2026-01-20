package com.jvictor01.app_setup;

import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.websockets.lcu.LcuWebsocketClient;
import com.jvictor01.websockets.lcu.WebSocketEventListener;

import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeagueConnectionsLifecycleManager implements WebSocketEventListener {
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private volatile LcuState lcuState = LcuState.DOWN;
    private final HttpWebClient httpWebClient;
    private LcuWebsocketClient lcuWebsocketClient;

    public LeagueConnectionsLifecycleManager(HttpWebClient httpWebClient) {
        this.httpWebClient = httpWebClient;
    }

    public void start() {
        service.scheduleWithFixedDelay(() -> {
            switch (lcuState) {

                case LcuState.DOWN -> {
                    if (lcuIsAvailable()) {
                        lcuState = LcuState.AVAILABLE;
                    }
                }

                case LcuState.AVAILABLE -> {
                    lcuWebsocketClient.connect();
                    lcuState = LcuState.WS_CONNECTED;
                }

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
