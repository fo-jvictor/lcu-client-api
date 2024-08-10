package com.jvictor01.lobby.team_builder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChampSelectSession {
    private int gameId;
    private Timer timer;
    private ChatDetails chatDetails;
    private List<TeamMember> myTeam;
    private List<TeamMember> theirTeam;
    private List<Trade> trades;
    private List<PickOrderSwap> pickOrderSwaps;
    private List<Action> actions;
    private int localPlayerCellId;
    private boolean allowSkinSelection;
    private boolean allowDuplicatePicks;
    private boolean allowBattleBoost;
    private int boostableSkinCount;
    private boolean allowRerolling;
    private int rerollsRemaining;
    private boolean allowLockedEvents;
    private int lockedEventIndex;
    private boolean benchEnabled;
    private List<BenchChampion> benchChampions;
    private int counter;
    private int recoveryCounter;
    private boolean skipChampionSelect;
    private boolean isSpectating;
    private boolean hasSimultaneousBans;
    private boolean hasSimultaneousPicks;

    public ChampSelectSession(JSONObject jsonObject) {
        this.gameId = jsonObject.getInt("gameId");
        this.timer = new Timer(jsonObject.getJSONObject("timer"));
        this.chatDetails = new ChatDetails(jsonObject.getJSONObject("chatDetails"));
        this.myTeam = parseTeam(jsonObject.getJSONArray("myTeam"));
        this.theirTeam = parseTeam(jsonObject.getJSONArray("theirTeam"));
        this.trades = parseTrades(jsonObject.getJSONArray("trades"));
        this.pickOrderSwaps = parsePickOrderSwaps(jsonObject.getJSONArray("pickOrderSwaps"));
        this.actions = parseActions(jsonObject.getJSONArray("actions"));
        this.localPlayerCellId = jsonObject.getInt("localPlayerCellId");
        this.allowSkinSelection = jsonObject.getBoolean("allowSkinSelection");
        this.allowDuplicatePicks = jsonObject.getBoolean("allowDuplicatePicks");
        this.allowBattleBoost = jsonObject.getBoolean("allowBattleBoost");
        this.boostableSkinCount = jsonObject.getInt("boostableSkinCount");
        this.allowRerolling = jsonObject.getBoolean("allowRerolling");
        this.rerollsRemaining = jsonObject.getInt("rerollsRemaining");
        this.allowLockedEvents = jsonObject.getBoolean("allowLockedEvents");
        this.lockedEventIndex = jsonObject.getInt("lockedEventIndex");
        this.benchEnabled = jsonObject.getBoolean("benchEnabled");
        this.benchChampions = parseBenchChampions(jsonObject.getJSONArray("benchChampions"));
        this.counter = jsonObject.getInt("counter");
        this.recoveryCounter = jsonObject.getInt("recoveryCounter");
        this.skipChampionSelect = jsonObject.getBoolean("skipChampionSelect");
        this.isSpectating = jsonObject.getBoolean("isSpectating");
        this.hasSimultaneousBans = jsonObject.getBoolean("hasSimultaneousBans");
        this.hasSimultaneousPicks = jsonObject.getBoolean("hasSimultaneousPicks");
    }

    public ChampSelectSession(){
    }

    private List<TeamMember> parseTeam(JSONArray jsonArray) {
        List<TeamMember> team = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            team.add(new TeamMember(jsonArray.getJSONObject(i)));
        }
        return team;
    }

    private List<Trade> parseTrades(JSONArray jsonArray) {
        List<Trade> trades = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            trades.add(new Trade(jsonArray.getJSONObject(i)));
        }
        return trades;
    }

    private List<PickOrderSwap> parsePickOrderSwaps(JSONArray jsonArray) {
        List<PickOrderSwap> pickOrderSwaps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            pickOrderSwaps.add(new PickOrderSwap(jsonArray.getJSONObject(i)));
        }
        return pickOrderSwaps;
    }

    private List<Action> parseActions(JSONArray jsonArray) {
        List<Action> actions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            actions.add(new Action(jsonArray.getJSONObject(i)));
        }
        return actions;
    }

    private List<BenchChampion> parseBenchChampions(JSONArray jsonArray) {
        List<BenchChampion> benchChampions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            benchChampions.add(new BenchChampion(jsonArray.getJSONObject(i)));
        }
        return benchChampions;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setChatDetails(ChatDetails chatDetails) {
        this.chatDetails = chatDetails;
    }

    public void setMyTeam(List<TeamMember> myTeam) {
        this.myTeam = myTeam;
    }

    public void setTheirTeam(List<TeamMember> theirTeam) {
        this.theirTeam = theirTeam;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    public void setPickOrderSwaps(List<PickOrderSwap> pickOrderSwaps) {
        this.pickOrderSwaps = pickOrderSwaps;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void setLocalPlayerCellId(int localPlayerCellId) {
        this.localPlayerCellId = localPlayerCellId;
    }

    public void setAllowSkinSelection(boolean allowSkinSelection) {
        this.allowSkinSelection = allowSkinSelection;
    }

    public void setAllowDuplicatePicks(boolean allowDuplicatePicks) {
        this.allowDuplicatePicks = allowDuplicatePicks;
    }

    public void setAllowBattleBoost(boolean allowBattleBoost) {
        this.allowBattleBoost = allowBattleBoost;
    }

    public void setBoostableSkinCount(int boostableSkinCount) {
        this.boostableSkinCount = boostableSkinCount;
    }

    public void setAllowRerolling(boolean allowRerolling) {
        this.allowRerolling = allowRerolling;
    }

    public void setRerollsRemaining(int rerollsRemaining) {
        this.rerollsRemaining = rerollsRemaining;
    }

    public void setAllowLockedEvents(boolean allowLockedEvents) {
        this.allowLockedEvents = allowLockedEvents;
    }

    public void setLockedEventIndex(int lockedEventIndex) {
        this.lockedEventIndex = lockedEventIndex;
    }

    public void setBenchEnabled(boolean benchEnabled) {
        this.benchEnabled = benchEnabled;
    }

    public void setBenchChampions(List<BenchChampion> benchChampions) {
        this.benchChampions = benchChampions;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setRecoveryCounter(int recoveryCounter) {
        this.recoveryCounter = recoveryCounter;
    }

    public void setSkipChampionSelect(boolean skipChampionSelect) {
        this.skipChampionSelect = skipChampionSelect;
    }

    public void setSpectating(boolean spectating) {
        isSpectating = spectating;
    }

    public void setHasSimultaneousBans(boolean hasSimultaneousBans) {
        this.hasSimultaneousBans = hasSimultaneousBans;
    }

    public void setHasSimultaneousPicks(boolean hasSimultaneousPicks) {
        this.hasSimultaneousPicks = hasSimultaneousPicks;
    }

    public int getGameId() {
        return gameId;
    }

    public Timer getTimer() {
        return timer;
    }

    public ChatDetails getChatDetails() {
        return chatDetails;
    }

    public List<TeamMember> getMyTeam() {
        return myTeam;
    }

    public List<TeamMember> getTheirTeam() {
        return theirTeam;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<PickOrderSwap> getPickOrderSwaps() {
        return pickOrderSwaps;
    }

    public List<Action> getActions() {
        return actions;
    }

    public int getLocalPlayerCellId() {
        return localPlayerCellId;
    }

    public boolean isAllowSkinSelection() {
        return allowSkinSelection;
    }

    public boolean isAllowDuplicatePicks() {
        return allowDuplicatePicks;
    }

    public boolean isAllowBattleBoost() {
        return allowBattleBoost;
    }

    public int getBoostableSkinCount() {
        return boostableSkinCount;
    }

    public boolean isAllowRerolling() {
        return allowRerolling;
    }

    public int getRerollsRemaining() {
        return rerollsRemaining;
    }

    public boolean isAllowLockedEvents() {
        return allowLockedEvents;
    }

    public int getLockedEventIndex() {
        return lockedEventIndex;
    }

    public boolean isBenchEnabled() {
        return benchEnabled;
    }

    public List<BenchChampion> getBenchChampions() {
        return benchChampions;
    }

    public int getCounter() {
        return counter;
    }

    public int getRecoveryCounter() {
        return recoveryCounter;
    }

    public boolean isSkipChampionSelect() {
        return skipChampionSelect;
    }

    public boolean isSpectating() {
        return isSpectating;
    }

    public boolean isHasSimultaneousBans() {
        return hasSimultaneousBans;
    }

    public boolean isHasSimultaneousPicks() {
        return hasSimultaneousPicks;
    }
}
