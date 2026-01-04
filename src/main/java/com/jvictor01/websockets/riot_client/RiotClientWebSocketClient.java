package com.jvictor01.websockets.riot_client;

import com.jvictor01.utils.trust_manager.SSLContextFactory;
import com.jvictor01.websockets.lcu.LcuWebsocketEvents;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

public class RiotClientWebSocketClient extends WebSocketClient {

    public RiotClientWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
        setSocketFactory(SSLContextFactory.createTrustAllSSLContext().getSocketFactory());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to Riot Client websocket.");
        this.send(LcuWebsocketEvents.ON_JSON_API_EVENT);
    }

    @Override
    public void onMessage(String s) {
    }

    @Override
    public void onMessage(ByteBuffer message) {
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
