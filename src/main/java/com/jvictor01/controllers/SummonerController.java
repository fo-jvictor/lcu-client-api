package com.jvictor01.controllers;

import com.jvictor01.summoners.dtos.BackgroundImageRequest;
import com.jvictor01.summoners.dtos.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.JsonBodyParser;
import com.jvictor01.utils.ResponseUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.http.HttpResponse;


public class SummonerController implements HttpHandler {
    private final SummonerService summonerService = new SummonerService();
    private final String basePath = "/api/summoners";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");

        String subpath = RequestRouteHelper.getSubpathByBaseRoute(basePath, exchange.getRequestURI().getRawPath());

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("/myself".equalsIgnoreCase(subpath)) {
            Summoner summoner = summonerService.loadOwnSummonerDetails();
            ResponseUtils.send(exchange, 200, summoner);
            return;
        }

        if ("/profile/background-skin".equalsIgnoreCase(subpath)) {
            BackgroundImageRequest backgroundImageRequest = JsonBodyParser.toDto(exchange, BackgroundImageRequest::new);
            HttpResponse<String> response = summonerService.changeSummonerBackgroundProfileImage(backgroundImageRequest);
            ResponseUtils.send(exchange, response.statusCode(), response.body());
        }

    }
}
