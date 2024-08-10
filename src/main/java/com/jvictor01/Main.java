package com.jvictor01;

import com.jvictor01.authentication.LeagueClientAuthentication;
import com.jvictor01.authentication.WebsocketAuthentication;
import com.jvictor01.gameflow.GameflowService;
import com.jvictor01.honeyfruit.HoneyFruitService;
import com.jvictor01.lobby.LobbyEndpoints;
import com.jvictor01.lobby.team_builder.LobbyTeamBuilderService;
import com.jvictor01.matchmaking.MatchmakingEndpoints;
import com.jvictor01.rso_auth.RsoAuthenticationService;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.teste.Controller;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;

import static com.jvictor01.authentication.LeagueClientAuthentication.AUTHORIZATION_TOKEN;
import static com.jvictor01.authentication.LeagueClientAuthentication.SERVER_PORT;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        LeagueClientAuthentication clientAuthentication = new LeagueClientAuthentication();
        clientAuthentication.connectToLCUApi();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext(LobbyEndpoints.LOBBY_V2_MATCHMAKING_SEARCH, new Controller());
        server.createContext(MatchmakingEndpoints.READY_CHECK_DECLINE, new Controller());
        server.setExecutor(null);
        server.start();
        System.out.println("SERVER STARTED ON PORT 8080");

        try {
            URI serverUri = new URI("wss://127.0.0.1:" + SERVER_PORT + "/");
            var headers = Map.of("Authorization", "Basic " + AUTHORIZATION_TOKEN);
            WebsocketAuthentication client = new WebsocketAuthentication(serverUri, headers);
            client.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
