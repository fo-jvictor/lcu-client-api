package com.jvictor01.gameflow;

public enum GameflowStateEnum {

    //All possible gameflow states
    TERMINATED_IN_ERROR("TerminatedInError"),
    END_OF_GAME("EndOfGame"),
    PRE_END_OF_GAME("PreEndOfGame"),
    WAITING_FOR_STATS("WaitingForStats"),
    RECONNECT("Reconnect"),
    IN_PROGRESS("InProgress"),
    FAILED_TO_LAUNCH("FailedToLaunch"),
    GAME_START("GameStart"),
    CHAMP_SELECT("ChampSelect"),
    READY_CHECK("ReadyCheck"),
    CHECKED_INTO_TOURNAMENT("CheckedIntoTournament"),
    MATCHMAKING("Matchmaking"),
    LOBBY("Lobby"),
    NONE("None");

    private final String state;

    GameflowStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public static GameflowStateEnum getFromString(String state) {
        for (GameflowStateEnum gameflowState : values()) {
            if (gameflowState.getState().equalsIgnoreCase(state)) {
                return gameflowState;
            }
        }
        return NONE;
    }
}
