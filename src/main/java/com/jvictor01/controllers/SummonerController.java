package com.jvictor01.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.BackgroundImageRequest;
import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.JsonBodyParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


public class SummonerController implements HttpHandler {
    private SummonerService summonerService = new SummonerService();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String basePath = "/summoners";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        String subpath = RequestRouteHelper.getSubpathByBaseRoute(basePath, exchange.getRequestURI().getRawPath());

        if ("/myself".equalsIgnoreCase(subpath)) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            Summoner summoner = summonerService.loadOwnSummonerDetails();
            String response = objectMapper.writeValueAsString(summoner);

            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            responseBody.write(response.getBytes());
            responseBody.close();
        }

        if ("/summoner-profile/background".equalsIgnoreCase(subpath)) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            BackgroundImageRequest backgroundImageRequest = JsonBodyParser.toDto(exchange, BackgroundImageRequest::new);
            var response = summonerService.changeSummonerBackgroundProfileImage(backgroundImageRequest);
            exchange.sendResponseHeaders(response.statusCode(), response.body().getBytes().length);
        }

    }
}
