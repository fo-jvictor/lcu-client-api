package com.jvictor01.chat;

public class ChatEndpoints {
    public static final String V1_ME = "/lol-chat/v1/me";
    public static final String SUSPEND_CHAT = "/chat/v1/suspend";
    public static final String RESUME_CHAT = "/chat/v1/resume";
    public static final String FRIEND_REQUESTS = "/lol-chat/v2/friend-requests";
    public static final String FRIEND_REQUEST_BY_PUUID = "/lol-chat/v2/friend-requests/";

    //endpoint from riot api, not lcu
    //can be used to fetch information from participants in the lobby in solo duo queue to grab nicknames
    public static final String CHAT_V5_PARTICIPANTS = "/chat/v5/participants";
}
