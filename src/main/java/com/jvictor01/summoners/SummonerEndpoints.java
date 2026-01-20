package com.jvictor01.summoners;

public class SummonerEndpoints {
    public static final String SUMMONER_DETAILS = "/lol-summoner/v2/summoners/names";
    public static final String CHANGE_RIOT_ID = "/lol-summoner/v1/save-alias";
    public static final String SUMMONER_BY_PUUID = "/lol-summoner/v2/summoners/puuid/";
    public static final String SUMMONER_BY_ID = "/lol-summoner/v1/summoners/";
    public static final String CURRENT_SUMMONER = "/lol-summoner/v1/current-summoner";
    public static final String SUMMONER_ALIAS_LOOKUP = "/lol-summoner/v1/alias/lookup";

    //sending a post to this endpoint with the following payload allows you to change background
    //image to whatever skin you want (you don't need to have the skin in your account)

    //payload: {
    //    "key": "backgroundSkinId",
    //    "value":103086
    //}

    //skins ids -> https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/skins.json
    public static final String SUMMONER_PROFILE_BACKGROUND = "/lol-summoner/v1/current-summoner/summoner-profile";
}
