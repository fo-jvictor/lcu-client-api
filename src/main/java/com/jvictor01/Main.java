package com.jvictor01;

import com.jvictor01.app_setup.LeagueApisSetup;
import com.jvictor01.app_setup.LeagueConnectionsLifecycleManager;
import com.jvictor01.app_setup.LeagueProcessUtils;
import com.jvictor01.chat.ChatController;
import com.jvictor01.controllers.*;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.riot_client.RiotSignOnService;
import com.jvictor01.websockets.frontend_connection.FrontendWebsocketConnection;
import com.jvictor01.websockets.lcu.LcuWebsocketClient;
import com.jvictor01.websockets.riot_client.RiotClientWebSocketClient;
import com.jvictor01.websockets.riot_messaging_service.RmsService;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

import static com.jvictor01.app_setup.LeagueApisSetup.*;

public class Main {
    public static void main(String[] args) throws IOException, AWTException {

        LeagueProcessUtils leagueProcessUtils = new LeagueProcessUtils();
        leagueProcessUtils.setUpLeagueClient();

        LeagueApisSetup leagueApisSetup = new LeagueApisSetup();
        leagueApisSetup.displayConnectionInfo();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/lobby", new LobbyController());
        server.createContext("/api/matchmaking", new MatchmakingController());
        server.createContext("/api/summoners", new SummonerController());
        server.createContext("/api/gameflow", new GameFlowController());
        server.createContext("/api/chat", new ChatController());
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(null);
        server.start();

        final FrontendWebsocketConnection websocketServer = new FrontendWebsocketConnection();
        websocketServer.connect();

        try {
            URI lcuWebSocket = new URI("wss://127.0.0.1:" + LCU_SERVER_PORT + "/");
            var lcuWebSocketHeaders = Map.of("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN);

            URI riotClientWebSocket = new URI("wss://127.0.0.1:" + RIOT_CLIENT_SERVER_PORT + "/");
            var riotClientWebSocketHeaders = Map.of("Authorization", "Basic " + RIOT_CLIENT_AUTH_TOKEN,
                    "Content-Type", "application/json",
                    "Accept", "application/json");


            var lifecycleManager = new LeagueConnectionsLifecycleManager(new HttpWebClient(), websocketServer);
            lifecycleManager.start();

            LcuWebsocketClient lcuWebsocketClient = new LcuWebsocketClient(lcuWebSocket, lcuWebSocketHeaders, websocketServer, lifecycleManager);
            lifecycleManager.setLcuWebsocketClient(lcuWebsocketClient);

            RiotClientWebSocketClient riotClientWebSocketClient = new RiotClientWebSocketClient(riotClientWebSocket, riotClientWebSocketHeaders);
            riotClientWebSocketClient.connect();

            RmsService rmsService = new RmsService(new RiotSignOnService());
            rmsService.setupRmsWebsocket();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (SystemTray.isSupported()) {
//            SystemTray tray = SystemTray.getSystemTray();
//            PopupMenu popup = new PopupMenu();
//            MenuItem exitItem = new MenuItem("Exit");
//            exitItem.addActionListener(e -> System.exit(0));
//            popup.add(exitItem);
//
//            Image image = ImageIO.read(Main.class.getResourceAsStream("/image.png"));
//            TrayIcon trayIcon = new TrayIcon(image, "league-client-enhancer", popup);
//            trayIcon.setImageAutoSize(true);
//            tray.add(trayIcon);
//        }

    }

}
