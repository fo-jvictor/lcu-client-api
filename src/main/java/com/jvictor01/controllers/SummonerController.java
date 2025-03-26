package com.jvictor01.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


public class SummonerController implements HttpHandler {

    private SummonerService summonerService;
    private ObjectMapper objectMapper;

    public SummonerController() {
        this.summonerService = new SummonerService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if ("/summoners/myself".equalsIgnoreCase(exchange.getRequestURI().toString())) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            Summoner summoner = summonerService.loadOwnSummonerDetails();
            String response = objectMapper.writeValueAsString(summoner);
            OutputStream responseBody = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, response.getBytes().length);
            responseBody.write(response.getBytes());
            responseBody.close();
        }

    }
}
