package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lobby {
    private Long id;
    private String name;
    private String description;
    private String gameMode;
    private String gameModeName;
    private String gameModeShortName;
    private String mapStringId;
    private boolean rotatingGameMode;

    public Lobby(JSONObject json) {
        this.id = json.getLong("id");
        this.name = json.getString("name");
        this.description = json.optString("description", "");
        this.gameMode = json.getString("gameMode");
        this.gameModeName = json.optString("gameModeName", "");
        this.gameModeShortName = json.optString("gameModeShortName", "");
        this.mapStringId = json.optString("mapStringId", "");
        this.rotatingGameMode = json.getBoolean("isRGM");
    }

    public Lobby() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameModeName() {
        return gameModeName;
    }

    public void setGameModeName(String gameModeName) {
        this.gameModeName = gameModeName;
    }

    public String getGameModeShortName() {
        return gameModeShortName;
    }

    public void setGameModeShortName(String gameModeShortName) {
        this.gameModeShortName = gameModeShortName;
    }

    public String getMapStringId() {
        return mapStringId;
    }

    public void setMapStringId(String mapStringId) {
        this.mapStringId = mapStringId;
    }

    public boolean isRotatingGameMode() {
        return rotatingGameMode;
    }

    public void setRotatingGameMode(boolean rotatingGameMode) {
        this.rotatingGameMode = rotatingGameMode;
    }
}
