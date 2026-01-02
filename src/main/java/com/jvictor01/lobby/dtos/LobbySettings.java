package com.jvictor01.lobby.dtos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LobbySettings {
    private List<Integer> allowablePremadeSizes;
    private List<String> customRewardsDisabledReasons;
    private List<String> customTeam100;
    private int queueId;

    public LobbySettings(JSONObject json) {
        this.allowablePremadeSizes = jsonArrayToList(json.getJSONArray("allowablePremadeSizes"));
        this.customRewardsDisabledReasons = jsonArrayToStringList(json.getJSONArray("customRewardsDisabledReasons"));
        this.customTeam100 = jsonArrayToStringList(json.getJSONArray("customTeam100"));
        this.queueId = json.getInt("queueId");
    }

    public LobbySettings() {

    }

    private List<Integer> jsonArrayToList(JSONArray jsonArray) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getInt(i));
        }
        return list;
    }

    private List<String> jsonArrayToStringList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    public void setAllowablePremadeSizes(List<Integer> allowablePremadeSizes) {
        this.allowablePremadeSizes = allowablePremadeSizes;
    }

    public void setCustomRewardsDisabledReasons(List<String> customRewardsDisabledReasons) {
        this.customRewardsDisabledReasons = customRewardsDisabledReasons;
    }

    public void setCustomTeam100(List<String> customTeam100) {
        this.customTeam100 = customTeam100;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public List<Integer> getAllowablePremadeSizes() {
        return allowablePremadeSizes;
    }

    public List<String> getCustomRewardsDisabledReasons() {
        return customRewardsDisabledReasons;
    }

    public List<String> getCustomTeam100() {
        return customTeam100;
    }

    public int getQueueId() {
        return queueId;
    }
}
