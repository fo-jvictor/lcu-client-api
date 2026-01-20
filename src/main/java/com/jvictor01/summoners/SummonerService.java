package com.jvictor01.summoners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.http.HttpMethods;
import com.jvictor01.http.HttpWebClient;
import com.jvictor01.summoners.dtos.BackgroundImageRequest;
import com.jvictor01.summoners.dtos.Summoner;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.Collections;

public class SummonerService {
    private final ObjectMapper objectMapper;
    private final HttpWebClient httpWebClient;

    public SummonerService() {
        this.objectMapper = new ObjectMapper();
        this.httpWebClient = new HttpWebClient();
    }

    public HttpResponse<String> changeSummonerBackgroundProfileImage(BackgroundImageRequest backgroundImageRequest) {
        return httpWebClient.buildRequestForLcu(SummonerEndpoints.SUMMONER_PROFILE_BACKGROUND, HttpMethods.POST, backgroundImageRequest);
    }

    //nickname format is: ingamename#tag
    public Summoner[] getSummonerDetailsByNickname(String nickname) {
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(Collections.singletonList(nickname));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = httpWebClient.buildRequestForLcu(SummonerEndpoints.SUMMONER_DETAILS, HttpMethods.POST, requestBody);

        try {
            return objectMapper.readValue(response.body(), Summoner[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public Summoner getSummonerByPuuid(String puuid) {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(SummonerEndpoints.SUMMONER_BY_PUUID + puuid, HttpMethods.GET);
        try {
            Summoner summoner = objectMapper.readValue(response.body(), Summoner.class);
            if (summoner == null) {
                throw new SummonerNotFoundException("Summoner not found by puuid: " + puuid);
            }
            return summoner;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Summoner getSummonerById(int id) {
        HttpResponse<String> response = httpWebClient.buildRequestForLcu(SummonerEndpoints.SUMMONER_BY_ID + id, HttpMethods.GET);
        try {
            return objectMapper.readValue(response.body(), Summoner.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSummonerPuuidByNicknameAndTagLine(String gameName, String tagLine) {
        HttpResponse<String> response =
                httpWebClient.buildRequestForLcu(SummonerEndpoints.SUMMONER_ALIAS_LOOKUP + "?gameName=" + gameName + "&tagLine=" + tagLine, HttpMethods.GET);

        try {
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode attributeNode = rootNode.path("puuid");

            if (!attributeNode.isMissingNode()) {
                return attributeNode.asText();
            }

            throw new RuntimeException("puuid not found for given gameName: " + gameName + " and tagLine: #" + tagLine);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    //Change riot id bypassing email validation
    public void changeRiotId(String newNickname, String newTagLine) {
        String requestBody = null;
        Summoner summoner = new Summoner();
        summoner.setGameName(newNickname);
        summoner.setTagLine(newTagLine);

        try {
            requestBody = objectMapper.writeValueAsString(summoner);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = httpWebClient.buildRequestForLcu(SummonerEndpoints.CHANGE_RIOT_ID, HttpMethods.POST, requestBody);
        System.out.println(response.body());

    }

    public Summoner loadOwnSummonerDetails() {
        HttpResponse<String> stringHttpResponse = httpWebClient.buildRequestForLcu(SummonerEndpoints.CURRENT_SUMMONER, HttpMethods.GET);
        JSONObject jsonObject = new JSONObject(stringHttpResponse.body());
        return new Summoner(jsonObject);
    }
}
