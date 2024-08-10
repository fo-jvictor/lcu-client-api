package com.jvictor01.honeyfruit;

import org.json.JSONObject;

public class AccountDetails {
    private boolean eligible;
    private String reasonCode;
    private String email;
    private String garenaPuuid;
    private String platformId;
    private String summonerName;
    private int summonerLevel;
    private int summonerIconId;
    private int garenaId;
    private boolean isReservedSummonerName;
    private boolean hasPlayedAGame;
    private String migrationStatus;

    public AccountDetails(JSONObject jsonObject) {
        JSONObject linkingStatus = jsonObject.getJSONObject("linking_status");
        this.eligible = linkingStatus.getBoolean("eligible");
        this.reasonCode = linkingStatus.getString("reason_code");
        this.email = linkingStatus.getString("email");

        JSONObject accountDetails = linkingStatus.getJSONObject("account_details");
        this.garenaPuuid = accountDetails.getString("garena_puuid");
        this.platformId = accountDetails.getString("platform_id");
        this.summonerName = accountDetails.getString("summoner_name");
        this.summonerLevel = accountDetails.getInt("summoner_level");
        this.summonerIconId = accountDetails.getInt("summoner_icon_id");
        this.garenaId = accountDetails.getInt("garena_id");
        this.isReservedSummonerName = accountDetails.getBoolean("is_reserved_summoner_name");
        this.hasPlayedAGame = accountDetails.getBoolean("has_played_a_game");

        this.migrationStatus = jsonObject.getString("migration_status");
    }

    public AccountDetails() {
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "eligible=" + eligible +
                ", reasonCode='" + reasonCode + '\'' +
                ", email='" + email + '\'' +
                ", garenaPuuid='" + garenaPuuid + '\'' +
                ", platformId='" + platformId + '\'' +
                ", summonerName='" + summonerName + '\'' +
                ", summonerLevel=" + summonerLevel +
                ", summonerIconId=" + summonerIconId +
                ", garenaId=" + garenaId +
                ", isReservedSummonerName=" + isReservedSummonerName +
                ", hasPlayedAGame=" + hasPlayedAGame +
                ", migrationStatus='" + migrationStatus + '\'' +
                '}';
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGarenaPuuid(String garenaPuuid) {
        this.garenaPuuid = garenaPuuid;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public void setSummonerIconId(int summonerIconId) {
        this.summonerIconId = summonerIconId;
    }

    public void setGarenaId(int garenaId) {
        this.garenaId = garenaId;
    }

    public void setReservedSummonerName(boolean reservedSummonerName) {
        isReservedSummonerName = reservedSummonerName;
    }

    public void setHasPlayedAGame(boolean hasPlayedAGame) {
        this.hasPlayedAGame = hasPlayedAGame;
    }

    public void setMigrationStatus(String migrationStatus) {
        this.migrationStatus = migrationStatus;
    }

    public boolean isEligible() {
        return eligible;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getEmail() {
        return email;
    }

    public String getGarenaPuuid() {
        return garenaPuuid;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public int getSummonerIconId() {
        return summonerIconId;
    }

    public int getGarenaId() {
        return garenaId;
    }

    public boolean isReservedSummonerName() {
        return isReservedSummonerName;
    }

    public boolean isHasPlayedAGame() {
        return hasPlayedAGame;
    }

    public String getMigrationStatus() {
        return migrationStatus;
    }
}
