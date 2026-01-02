package com.jvictor01.websockets.riot_messaging_service;

import com.jvictor01.utils.trust_manager.SSLContextFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;

public class RmsWebsocketClient extends WebSocketClient {
    private final RmsMessageDecoder rmsMessageDecoder;

    public RmsWebsocketClient(URI serverUri, RmsMessageDecoder rmsMessageDecoder) {
        super(serverUri);
        setSocketFactory(SSLContextFactory.createTrustAllSSLContext().getSocketFactory());
        this.rmsMessageDecoder = rmsMessageDecoder;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("RMS Websocket connected");
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void onMessage(ByteBuffer message) {
        String json = rmsMessageDecoder.decode(message);
        //System.out.println("RMS Message:\n" + json);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("RMS closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}
