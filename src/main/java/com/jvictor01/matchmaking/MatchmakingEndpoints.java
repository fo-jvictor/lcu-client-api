package com.jvictor01.matchmaking;

public class MatchmakingEndpoints {
    public static final String READY_CHECK = "/lol-matchmaking/v1/ready-check";
    public static final String READY_CHECK_ACCEPT = "/lol-matchmaking/v1/ready-check/accept";
    public static final String READY_CHECK_DECLINE = "/lol-matchmaking/v1/ready-check/decline";
    public static final String searchErrors = "/lol-matchmaking/v1/search/errors";
    public static final String searchErrorsById = "/lol-matchmaking/v1/search/errors/{id}";
    public static final String postLobby = "/lol-lobby/v2/lobby";
    public static final String MATCHMAKING_SEARCH = "/lol-matchmaking/v1/search";

}
