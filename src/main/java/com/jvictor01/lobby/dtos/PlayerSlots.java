package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerSlots {
    private String positionPreference;

    public PlayerSlots(){}

    public PlayerSlots(String positionPreference) {
        this.positionPreference = positionPreference;
    }

    public String getPositionPreference() {
        return positionPreference;
    }

    public void setPositionPreference(String positionPreference) {
        this.positionPreference = positionPreference;
    }
}
