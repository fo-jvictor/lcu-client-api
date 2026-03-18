package com.jvictor01.lobby.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchState {
    private LowPriorityData lowPriorityData;
    private String searchState;

    public SearchState(){}

    public String getSearchState() {
        return searchState;
    }

    public void setSearchState(String searchState) {
        this.searchState = searchState;
    }

    public LowPriorityData getLowPriorityData() {
        return lowPriorityData;
    }

    public void setLowPriorityData(LowPriorityData lowPriorityData) {
        this.lowPriorityData = lowPriorityData;
    }
}
