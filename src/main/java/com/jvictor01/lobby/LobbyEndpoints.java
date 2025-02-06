package com.jvictor01.lobby;

public class LobbyEndpoints {
    public static final String LOBBY_V2_MATCHMAKING_SEARCH = "/lol-lobby/v2/lobby/matchmaking/search";
    public static final String PLAY_AGAIN = "/lol-lobby/v2/play-again";
    public static final String POSITION_PREFERENCES = "/lol-lobby/v2/lobby/members/localMember/position-preferences";
    public static final String LOBBY_V2_MEMBERS = "/lol-lobby/v2/lobby/members";
    public static final String LOBBY_V2 = "/lol-lobby/v2/lobby";
    public static final String INVITATIONS_V2 = "/lol-lobby/v2/lobby/invitations";
    public static final String KICK_SUMMONER_BY_SUMMONER_ID = "/lol-lobby/v2/lobby/members/%s/kick";

    //endpoint from riot api, not lcu
    //can be used to fetch information from participants in the lobby in solo duo queue to grab nicknames
    public static final String CHAT_V5_PARTICIPANTS = "/chat/v5/participants";
}
