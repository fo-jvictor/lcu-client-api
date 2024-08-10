package com.jvictor01.summoners;

import org.json.JSONObject;

public class Summoner {
    private int accountId;
    private String displayName;
    private String gameName;
    private String internalName;
    private boolean nameChangeFlag;
    private int percentCompleteForNextLevel;
    private String privacy;
    private int profileIconId;
    private String puuid;
    private RerollPoints rerollPoints;
    private int summonerId;
    private int summonerLevel;
    private String tagLine;
    private boolean unnamed;
    private int xpSinceLastLevel;
    private int xpUntilNextLevel;

    public Summoner(JSONObject jsonObject) {
        this.accountId = jsonObject.getInt("accountId");
        this.displayName = jsonObject.getString("displayName");
        this.gameName = jsonObject.getString("gameName");
        this.internalName = jsonObject.getString("internalName");
        this.nameChangeFlag = jsonObject.getBoolean("nameChangeFlag");
        this.percentCompleteForNextLevel = jsonObject.getInt("percentCompleteForNextLevel");
        this.privacy = jsonObject.getString("privacy");
        this.profileIconId = jsonObject.getInt("profileIconId");
        this.puuid = jsonObject.getString("puuid");
        this.summonerId = jsonObject.getInt("summonerId");
        this.summonerLevel = jsonObject.getInt("summonerLevel");
        this.tagLine = jsonObject.getString("tagLine");
        this.unnamed = jsonObject.getBoolean("unnamed");
        this.xpSinceLastLevel = jsonObject.getInt("xpSinceLastLevel");
        this.xpUntilNextLevel = jsonObject.getInt("xpUntilNextLevel");
        this.rerollPoints = new RerollPoints(jsonObject.getJSONObject("rerollPoints"));
    }

    public Summoner() {
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setNameChangeFlag(boolean nameChangeFlag) {
        this.nameChangeFlag = nameChangeFlag;
    }

    public void setPercentCompleteForNextLevel(int percentCompleteForNextLevel) {
        this.percentCompleteForNextLevel = percentCompleteForNextLevel;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setRerollPoints(RerollPoints rerollPoints) {
        this.rerollPoints = rerollPoints;
    }

    public void setSummonerId(int summonerId) {
        this.summonerId = summonerId;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public void setUnnamed(boolean unnamed) {
        this.unnamed = unnamed;
    }

    public void setXpSinceLastLevel(int xpSinceLastLevel) {
        this.xpSinceLastLevel = xpSinceLastLevel;
    }

    public void setXpUntilNextLevel(int xpUntilNextLevel) {
        this.xpUntilNextLevel = xpUntilNextLevel;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGameName() {
        return gameName;
    }

    public String getInternalName() {
        return internalName;
    }

    public boolean isNameChangeFlag() {
        return nameChangeFlag;
    }

    public int getPercentCompleteForNextLevel() {
        return percentCompleteForNextLevel;
    }

    public String getPrivacy() {
        return privacy;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public String getPuuid() {
        return puuid;
    }

    public RerollPoints getRerollPoints() {
        return rerollPoints;
    }

    public int getSummonerId() {
        return summonerId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public String getTagLine() {
        return tagLine;
    }

    public boolean isUnnamed() {
        return unnamed;
    }

    public int getXpSinceLastLevel() {
        return xpSinceLastLevel;
    }

    public int getXpUntilNextLevel() {
        return xpUntilNextLevel;
    }

    @Override
    public String toString() {
        return "Summoner{" +
                "accountId=" + accountId +
                ", displayName='" + displayName + '\'' +
                ", gameName='" + gameName + '\'' +
                ", internalName='" + internalName + '\'' +
                ", nameChangeFlag=" + nameChangeFlag +
                ", percentCompleteForNextLevel=" + percentCompleteForNextLevel +
                ", privacy='" + privacy + '\'' +
                ", profileIconId=" + profileIconId +
                ", puuid='" + puuid + '\'' +
                ", rerollPoints=" + rerollPoints +
                ", summonerId=" + summonerId +
                ", summonerLevel=" + summonerLevel +
                ", tagLine='" + tagLine + '\'' +
                ", unnamed=" + unnamed +
                ", xpSinceLastLevel=" + xpSinceLastLevel +
                ", xpUntilNextLevel=" + xpUntilNextLevel +
                '}';
    }
}
