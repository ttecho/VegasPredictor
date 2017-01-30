package com.github.techo.service;

import com.github.techo.entities.Matchup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchupService {

    @Autowired
    private TeamService teamService;

    public Matchup getMatchup(String teamUrl, String teamName,
                              String opponentUrl, String opponentName) {
        return new Matchup(
                teamService.calculateTeam(teamUrl, teamName),
                teamService.calculateTeam(opponentUrl, opponentName)
        );
    }
}
