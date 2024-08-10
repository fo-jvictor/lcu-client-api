package com.jvictor01.honeyfruit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.net.http.HttpResponse;

public class HoneyFruitService {
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;
    private final SummonerService summonerService;

    public HoneyFruitService() {
        this.objectMapper = new ObjectMapper();
        this.httpUtils = new HttpUtils();
        this.summonerService = new SummonerService();
    }

    //endpoint nao ta funcionando?
    public AccountDetails getHoneyFruit(String nickname) {
        Summoner[] summoners = summonerService.getSummonerDetailsByNickname(nickname);
        HttpResponse<String> response = httpUtils.buildGetRequest("/lol-honeyfruit/v1/account-claim/account-status/"
                + summoners[0].getPuuid() + "?puuid=" + summoners[0].getPuuid());
        try {
            return objectMapper.readValue(response.body(), AccountDetails.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
