package com.jvictor01.http;

public enum AvailableApis {
    LCU("Lcu Api"),
    RIOT("Riot Api"),
    RIOT_CLIENT("Riot Client Api");

    private String value;

    AvailableApis(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
