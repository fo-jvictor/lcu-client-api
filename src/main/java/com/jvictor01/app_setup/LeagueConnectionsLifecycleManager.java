package com.jvictor01.app_setup;

import com.jvictor01.utils.HttpUtils;
import com.jvictor01.websockets.lcu.LcuWebsocketClient;
import com.jvictor01.websockets.lcu.WebSocketEventListener;

import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeagueConnectionsLifecycleManager implements WebSocketEventListener {
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private volatile LcuState lcuState = LcuState.DOWN;
    private final HttpUtils httpUtils;
    private LcuWebsocketClient lcuWebsocketClient;

    public LeagueConnectionsLifecycleManager(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
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
            HttpResponse<String> stringHttpResponse = httpUtils.buildGetRequest("/lol-platform-config/v1/initial-configuration-complete");
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
