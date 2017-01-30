package com.github.techo.service;

import com.github.techo.entities.Game;
import com.github.techo.entities.GameResult;
import com.github.techo.entities.Points;
import com.github.techo.entities.Scores;
import com.github.techo.entities.TeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class StatService {

    @Autowired
    VegasService vegasService;

    TeamStats getTeamStats(List<Game> playedGames) {
        return TeamStats.builder()
                .scores(calculateScores(playedGames))
                .vegas(vegasService.getVegasStats(playedGames))
                .build();
    }

    private Scores calculateScores(List<Game> playedGames) {
        return Scores.builder()
                .scored(calculatePointsFor(playedGames))
                .against(calculatePointsAgainst(playedGames))
                .build();
    }

    private Points calculatePointsFor(List<Game> playedGames) {
        return Points.builder()
                .average(calculateAveragePointsFor(playedGames))
                .all(playedGames
                        .stream()
                        .map(Game::getGameResult)
                        .map(GameResult::getPoints)
                        .collect(Collectors.toList())
                )
                .build();

    }

    private Points calculatePointsAgainst(List<Game> playedGames) {
        return Points.builder()
                .average(calculateAveragePointsAgainst(playedGames))
                .all(playedGames
                        .stream()
                        .map(Game::getGameResult)
                        .map(GameResult::getOpponentPoints)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private double calculateAveragePointsAgainst(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getGameResult)
                .mapToDouble(GameResult::getOpponentPoints)
                .average()
                .getAsDouble();
    }

    private double calculateAveragePointsFor(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getGameResult)
                .mapToDouble(GameResult::getPoints)
                .average()
                .getAsDouble();
    }
}
