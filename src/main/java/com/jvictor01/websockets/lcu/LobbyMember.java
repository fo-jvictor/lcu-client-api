package com.jvictor01.websockets.lcu;

public class LobbyMember {
    private Long summonerId;
    private String summonerName;
    private String firstRole;
    private String secondRole;

    public LobbyMember(Long summonerId, String summonerName, String firstRole, String secondRole) {
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.firstRole = firstRole;
        this.secondRole = secondRole;
    }

    public Long getSummonerId() {
        return summonerId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public String getFirstRole() {
        return firstRole;
    }

    public String getSecondRole() {
        return secondRole;
    }

}
