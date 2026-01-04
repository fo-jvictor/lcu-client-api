package com.jvictor01;

import com.jvictor01.app_setup.LeagueProcessUtils;
import com.jvictor01.controllers.*;
import com.jvictor01.riot_client.RiotSignOnService;
import com.jvictor01.utils.trust_manager.SSLContextFactory;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import com.jvictor01.websockets.lcu.LcuWebsocketClient;
import com.jvictor01.websockets.riot_client.RiotClientWebSocketClient;
import com.jvictor01.websockets.riot_messaging_service.RmsMessageDecoder;
import com.jvictor01.websockets.riot_messaging_service.RmsWebsocketClient;
import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

import static com.jvictor01.app_setup.LeagueApisSetup.*;

public class Main {
    public static void main(String[] args) throws IOException {

        LeagueProcessUtils leagueProcessUtils = new LeagueProcessUtils();
        leagueProcessUtils.setUpLeagueClient();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/lobby", new LobbyController());
        server.createContext("/api/matchmaking", new MatchmakingController());
        server.createContext("/api/summoners", new SummonerController());
        server.createContext("/api/gameflow", new GameFlowController());
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080");

        final FrontendWebsocketConnection websocketServer = new FrontendWebsocketConnection();
        websocketServer.connect();

        try {
            URI lcuWebSocket = new URI("wss://127.0.0.1:" + LCU_SERVER_PORT + "/");
            var lcuWebSocketHeaders = Map.of("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN);

            URI riotClientWebSocket = new URI("wss://127.0.0.1:" + RIOT_CLIENT_SERVER_PORT + "/");
            var riotClientWebSocketHeaders = Map.of("Authorization", "Basic " + RIOT_CLIENT_AUTH_TOKEN,
                    "Content-Type", "application/json",
                    "Accept", "application/json");

            SSLContext context = SSLContextFactory.createTrustAllSSLContext();

            LcuWebsocketClient lcuWebsocketClient = new LcuWebsocketClient(lcuWebSocket, lcuWebSocketHeaders, websocketServer);
            lcuWebsocketClient.setSocketFactory(context.getSocketFactory());
            lcuWebsocketClient.connectBlocking();

            RiotClientWebSocketClient riotClientWebSocketClient = new RiotClientWebSocketClient(riotClientWebSocket, riotClientWebSocketHeaders);
            riotClientWebSocketClient.setSocketFactory(context.getSocketFactory());
            riotClientWebSocketClient.connectBlocking();

            RiotSignOnService riotSignOnService = new RiotSignOnService();
            String accessToken = riotSignOnService.getAccessToken();

            String rmsUrl = String.format("wss://us.edge.rms.si.riotgames.com:443/rms/v1/session" +
                            "?token=%s" +
                            "&id=%s" +
                            "&token_type=access" +
                            "&product_id=riot_client" +
                            "&platform=windows" +
                            "&device=desk",
                    accessToken, UUID.randomUUID());

            URI uri = URI.create(rmsUrl);
            RmsWebsocketClient rmsClient = new RmsWebsocketClient(uri, new RmsMessageDecoder());
            rmsClient.connectBlocking();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
