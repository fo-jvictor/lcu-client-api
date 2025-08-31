package com.jvictor01;

import com.jvictor01.authentication.LeagueClientAuthentication;
import com.jvictor01.authentication.WebsocketAuthentication;
import com.jvictor01.controllers.GameFlowController;
import com.jvictor01.controllers.LobbyController;
import com.jvictor01.controllers.MatchmakingController;
import com.jvictor01.controllers.SummonerController;
import com.jvictor01.frontend.FrontendWebsocketServer;
import com.jvictor01.trust_manager.SSLContextFactory;
import com.jvictor01.utils.LeagueProcessUtils;
import com.sun.net.httpserver.HttpServer;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

import static com.jvictor01.authentication.LeagueClientAuthentication.LCU_AUTHORIZATION_TOKEN;
import static com.jvictor01.authentication.LeagueClientAuthentication.LCU_SERVER_PORT;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        LeagueProcessUtils.setUpLeagueClient();
        LeagueClientAuthentication clientAuthentication = new LeagueClientAuthentication();

        clientAuthentication.connectToLCUApi();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/lobby", new LobbyController());
        server.createContext("/matchmaking", new MatchmakingController());
        server.createContext("/summoners", new SummonerController());
        server.createContext("/gameflow", new GameFlowController());

        server.setExecutor(null);
        server.start();
        System.out.println("SERVER STARTED ON PORT 8080");

        final FrontendWebsocketServer websocketServer = new FrontendWebsocketServer();
        websocketServer.connect();


        try {
            URI serverUri = new URI("wss://127.0.0.1:" + LCU_SERVER_PORT + "/");
            var headers = Map.of("Authorization", "Basic " + LCU_AUTHORIZATION_TOKEN);

            SSLContext context = SSLContextFactory.createTrustAllSSLContext();

            WebsocketAuthentication client = new WebsocketAuthentication(serverUri, headers, websocketServer);
            client.setSocketFactory(context.getSocketFactory());
            client.connectBlocking();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
