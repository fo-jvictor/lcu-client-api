package com.jvictor01.lobby.team_builder.dtos;

import org.json.JSONObject;

public class TeamMember {
    private int cellId;
    private int championId;
    private int selectedSkinId;
    private int wardSkinId;
    private int spell1Id;
    private int spell2Id;
    private int team;
    private String assignedPosition;
    private int championPickIntent;
    private String playerType;
    private int summonerId;
    private String puuid;
    private String nameVisibilityType;
    private int obfuscatedSummonerId;
    private String obfuscatedPuuid;

    public TeamMember(JSONObject jsonObject) {
        this.cellId = jsonObject.getInt("cellId");
        this.championId = jsonObject.getInt("championId");
        this.selectedSkinId = jsonObject.getInt("selectedSkinId");
        this.wardSkinId = jsonObject.getInt("wardSkinId");
        this.spell1Id = jsonObject.getInt("spell1Id");
        this.spell2Id = jsonObject.getInt("spell2Id");
        this.team = jsonObject.getInt("team");
        this.assignedPosition = jsonObject.getString("assignedPosition");
        this.championPickIntent = jsonObject.getInt("championPickIntent");
        this.playerType = jsonObject.getString("playerType");
        this.summonerId = jsonObject.getInt("summonerId");
        this.puuid = jsonObject.getString("puuid");
        this.nameVisibilityType = jsonObject.getString("nameVisibilityType");
        this.obfuscatedSummonerId = jsonObject.getInt("obfuscatedSummonerId");
        this.obfuscatedPuuid = jsonObject.getString("obfuscatedPuuid");
    }

    public TeamMember() {

    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public void setSelectedSkinId(int selectedSkinId) {
        this.selectedSkinId = selectedSkinId;
    }

    public void setWardSkinId(int wardSkinId) {
        this.wardSkinId = wardSkinId;
    }

    public void setSpell1Id(int spell1Id) {
        this.spell1Id = spell1Id;
    }

    public void setSpell2Id(int spell2Id) {
        this.spell2Id = spell2Id;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setAssignedPosition(String assignedPosition) {
        this.assignedPosition = assignedPosition;
    }

    public void setChampionPickIntent(int championPickIntent) {
        this.championPickIntent = championPickIntent;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public void setSummonerId(int summonerId) {
        this.summonerId = summonerId;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setNameVisibilityType(String nameVisibilityType) {
        this.nameVisibilityType = nameVisibilityType;
    }

    public void setObfuscatedSummonerId(int obfuscatedSummonerId) {
        this.obfuscatedSummonerId = obfuscatedSummonerId;
    }

    public void setObfuscatedPuuid(String obfuscatedPuuid) {
        this.obfuscatedPuuid = obfuscatedPuuid;
    }

    public int getCellId() {
        return cellId;
    }

    public int getChampionId() {
        return championId;
    }

    public int getSelectedSkinId() {
        return selectedSkinId;
    }

    public int getWardSkinId() {
        return wardSkinId;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public int getTeam() {
        return team;
    }

    public String getAssignedPosition() {
        return assignedPosition;
    }

    public int getChampionPickIntent() {
        return championPickIntent;
    }

    public String getPlayerType() {
        return playerType;
    }

    public int getSummonerId() {
        return summonerId;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getNameVisibilityType() {
        return nameVisibilityType;
    }

    public int getObfuscatedSummonerId() {
        return obfuscatedSummonerId;
    }

    public String getObfuscatedPuuid() {
        return obfuscatedPuuid;
    }
}
