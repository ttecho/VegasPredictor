package com.github.techo.service;

import com.github.techo.entities.Game;
import com.github.techo.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TeamService {
    @Autowired
    private GamesService gamesService;

    @Autowired
    private StatService statService;

    Team calculateTeam(String teamUrl, String teamName) {
        List<Game> playedGames = gamesService.getPlayedGames(teamUrl, teamName);
        return Team.builder()
                .teamName(teamName)
                .games(playedGames)
                .stats(statService.getTeamStats(playedGames))
                .build();
    }
}
