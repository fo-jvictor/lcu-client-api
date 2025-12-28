package com.jvictor01.websockets.lcu;

public class LcuWebsocketEvents {

    //5 means we are subscribing the following string is the event we are subscribing
    public static final String LOBBY_INFORMATION = """
            [5, "OnJsonApiEvent_lol-lobby_v2_lobby"]
            """;

    //used to subscribe to all events in lcu websocket
    public static final String ON_JSON_API_EVENT = """
            [5, "OnJsonApiEvent"]
            """;

    public static final String MATCHMAKING_READY_CHECK = """
            [5, "OnJsonApiEvent_lol-matchmaking_v1_search"]
            """;
}
