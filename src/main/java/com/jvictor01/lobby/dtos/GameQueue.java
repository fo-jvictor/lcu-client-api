package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameQueue {
    private Long id;
    private String name;
    private String description;
    private String gameMode;
    @JsonProperty("isEnabled")
    private boolean enabled;
    @JsonProperty("isVisible")
    private boolean visible;
    private boolean removalFromGameAllowed;
    private String category;
    private String gameSelectCategory;
    @JsonProperty("isCustom")
    private boolean custom;
    private String queueAvailability; //"PlatformDisabled" ou "Available"
    private String type;

    public GameQueue() {
    }

    public boolean isRemovalFromGameAllowed() {
        return removalFromGameAllowed;
    }

    public void setRemovalFromGameAllowed(boolean removalFromGameAllowed) {
        this.removalFromGameAllowed = removalFromGameAllowed;
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGameSelectCategory() {
        return gameSelectCategory;
    }

    public void setGameSelectCategory(String gameSelectCategory) {
        this.gameSelectCategory = gameSelectCategory;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getQueueAvailability() {
        return queueAvailability;
    }

    public void setQueueAvailability(String queueAvailability) {
        this.queueAvailability = queueAvailability;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GameQueue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gameMode='" + gameMode + '\'' +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", removalFromGameAllowed=" + removalFromGameAllowed +
                ", type='" + type + '\'' +
                '}';
    }
}
