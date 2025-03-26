package com.jvictor01.summoners;

import org.json.JSONObject;

public class Summoner {
    private Long accountId;
    private String displayName;
    private String gameName;
    private String internalName;
    private boolean nameChangeFlag;
    private Long percentCompleteForNextLevel;
    private String privacy;
    private Long profileIconId;
    private String puuid;
    private RerollPoints rerollPoints;
    private Long summonerId;
    private Long summonerLevel;
    private String tagLine;
    private boolean unnamed;
    private Long xpSinceLastLevel;
    private Long xpUntilNextLevel;

    public Summoner(JSONObject jsonObject) {
        this.accountId = jsonObject.getLong("accountId");
        this.displayName = jsonObject.getString("displayName");
        this.gameName = jsonObject.getString("gameName");
        this.internalName = jsonObject.getString("internalName");
        this.nameChangeFlag = jsonObject.getBoolean("nameChangeFlag");
        this.percentCompleteForNextLevel = jsonObject.getLong("percentCompleteForNextLevel");
        this.privacy = jsonObject.getString("privacy");
        this.profileIconId = jsonObject.getLong("profileIconId");
        this.puuid = jsonObject.getString("puuid");
        this.summonerId = jsonObject.getLong("summonerId");
        this.summonerLevel = jsonObject.getLong("summonerLevel");
        this.tagLine = jsonObject.getString("tagLine");
        this.unnamed = jsonObject.getBoolean("unnamed");
        this.xpSinceLastLevel = jsonObject.getLong("xpSinceLastLevel");
        this.xpUntilNextLevel = jsonObject.getLong("xpUntilNextLevel");
        this.rerollPoints = new RerollPoints(jsonObject.getJSONObject("rerollPoints"));
    }

    public Summoner() {
    }

    public void setAccountId(Long accountId) {
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

    public void setPercentCompleteForNextLevel(Long percentCompleteForNextLevel) {
        this.percentCompleteForNextLevel = percentCompleteForNextLevel;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setProfileIconId(Long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public void setRerollPoints(RerollPoints rerollPoints) {
        this.rerollPoints = rerollPoints;
    }

    public void setSummonerId(Long summonerId) {
        this.summonerId = summonerId;
    }

    public void setSummonerLevel(Long summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public void setUnnamed(boolean unnamed) {
        this.unnamed = unnamed;
    }

    public void setXpSinceLastLevel(Long xpSinceLastLevel) {
        this.xpSinceLastLevel = xpSinceLastLevel;
    }

    public void setXpUntilNextLevel(Long xpUntilNextLevel) {
        this.xpUntilNextLevel = xpUntilNextLevel;
    }

    public Long getAccountId() {
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

    public Long getPercentCompleteForNextLevel() {
        return percentCompleteForNextLevel;
    }

    public String getPrivacy() {
        return privacy;
    }

    public Long getProfileIconId() {
        return profileIconId;
    }

    public String getPuuid() {
        return puuid;
    }

    public RerollPoints getRerollPoints() {
        return rerollPoints;
    }

    public Long getSummonerId() {
        return summonerId;
    }

    public Long getSummonerLevel() {
        return summonerLevel;
    }

    public String getTagLine() {
        return tagLine;
    }

    public boolean isUnnamed() {
        return unnamed;
    }

    public Long getXpSinceLastLevel() {
        return xpSinceLastLevel;
    }

    public Long getXpUntilNextLevel() {
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
