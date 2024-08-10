package com.jvictor01.lobby.team_builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvictor01.summoners.Summoner;
import com.jvictor01.summoners.SummonerService;
import com.jvictor01.utils.HttpUtils;

import java.util.List;

import static com.jvictor01.lobby.team_builder.LobbyTeamBuilderEndpoints.CHAMP_SELECT_SESSION;
import static com.jvictor01.lobby.team_builder.LobbyTeamBuilderEndpoints.MY_SELECTION;

public class LobbyTeamBuilderService {
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;
    private final SummonerService summonerService;

    public LobbyTeamBuilderService() {
        this.httpUtils = new HttpUtils();
        this.objectMapper = new ObjectMapper();
        this.summonerService = new SummonerService();
    }

    public ChampSelectSession getChampSelection() {
        return httpUtils.buildGetRequestBy(CHAMP_SELECT_SESSION, ChampSelectSession.class);
    }

    public TeamMember getMySelection() {
        return httpUtils.buildGetRequestBy(MY_SELECTION, TeamMember.class);
    }

    public TeamMember postMySelection() {
        TeamMember teamMember = new TeamMember();
        return httpUtils.buildPostRequestBy(MY_SELECTION, teamMember, TeamMember.class);
    }

    public void papoi() {

        List<TeamMember> myTeam = getChampSelection().getMyTeam();
        myTeam.forEach(teamMember -> {
            Summoner summonerByPuuid = summonerService.getSummonerById(teamMember.getSummonerId());
            System.out.println("Summoner id: " + summonerByPuuid.getSummonerId() + "nickname: " + summonerByPuuid.getDisplayName());
        });

    }
}
