package com.jvictor01.summoners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;
import java.util.Collections;

public class SummonerService {
    private final ObjectMapper objectMapper;
    private final HttpUtils httpUtils;

    public SummonerService() {
        this.objectMapper = new ObjectMapper();
        this.httpUtils = new HttpUtils();
    }

    //nickname format is: ingamename#tag
    public Summoner[] getSummonerDetailsByNickname(String nickname) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(Collections.singletonList(nickname));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = httpUtils.buildPostRequest(SummonerEndpoints.getSummonerDetailsEndpoint, requestBody);

        try {
            return objectMapper.readValue(response.body(), Summoner[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public Summoner getSummonerByPuuid(String puuid) {
        HttpResponse<String> response = httpUtils.buildGetRequest(SummonerEndpoints.SUMMONER_BY_PUUID + puuid);
        try {
            return objectMapper.readValue(response.body(), Summoner.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Summoner getSummonerById(int id) {
        HttpResponse<String> response = httpUtils.buildGetRequest(SummonerEndpoints.SUMMONER_BY_ID + id);
        try {
            return objectMapper.readValue(response.body(), Summoner.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //Change riot id without bypassing email validation
    public void changeRiotId(String nickname, String tagline) {
        String requestBody = null;
        Summoner summoner = new Summoner();
        summoner.setGameName(nickname);
        summoner.setTagLine(tagline);

        try {
            requestBody = objectMapper.writeValueAsString(summoner);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = httpUtils.buildPostRequest(SummonerEndpoints.changeRiotIdEndpoint, requestBody);
        System.out.println(response.body());

    }

    public Summoner loadOwnSummonerDetails() {
        return httpUtils.buildGetRequestBy(SummonerEndpoints.CURRENT_SUMMONER, Summoner.class);
    }
}
