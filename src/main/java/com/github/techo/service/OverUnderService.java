package com.github.techo.service;

import com.github.techo.entities.Average;
import com.github.techo.entities.Game;
import com.github.techo.entities.OverUnder;
import com.github.techo.entities.OverUnderEnum;
import com.github.techo.entities.OverUnderRecord;
import com.github.techo.entities.OverUnderStats;
import com.github.techo.entities.Vegas;
import com.github.techo.entities.VegasProjection;
import com.github.techo.entities.VegasResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.techo.util.Calculation.calculateAverage;

@Service
class OverUnderService {

    OverUnderStats getOverUnderStats(List<Game> playedGames) {
        OverUnderStats overUnderStats = new OverUnderStats();

        overUnderStats.setAll(getActualsForOverUnder(playedGames));
        overUnderStats.setAverages(
                new Average(
                        calculateAverageOverUnder(playedGames),
                        calculateAverage(overUnderStats.getAll()),
                        calculateAverageDifferenceFromOverUnder(playedGames)
                )
        );

        overUnderStats.setRecord(getOverUnderRecord(playedGames));
        return overUnderStats;
    }

    private List<Integer> getActualsForOverUnder(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getOverUnder)
                .map(OverUnder::getActual)
                .collect(Collectors.toList());
    }

    private double calculateAverageOverUnder(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getProjection)
                .mapToDouble(VegasProjection::getOverUnder)
                .average()
                .getAsDouble();
    }

    private double calculateAverageDifferenceFromOverUnder(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getOverUnder)
                .map(OverUnder::getActual)
                .mapToDouble(x -> x)
                .average()
                .getAsDouble();
    }

    private OverUnderRecord getOverUnderRecord(List<Game> playedGames) {
        OverUnderRecord overUnderRecord = new OverUnderRecord();
        overUnderRecord.setOver(calculateOverUnderOvers(playedGames));
        overUnderRecord.setPush(calculateOverUnderPushes(playedGames));
        overUnderRecord.setUnder(calculateOverUnderUnders(playedGames));
        return overUnderRecord;
    }

    private long calculateOverUnderOvers(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getOverUnder)
                .filter(result -> result.getResult() == OverUnderEnum.OVER)
                .count();
    }

    private long calculateOverUnderPushes(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getOverUnder)
                .filter(result -> result.getResult() == OverUnderEnum.PUSH)
                .count();
    }

    private long calculateOverUnderUnders(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getOverUnder)
                .filter(result -> result.getResult() == OverUnderEnum.UNDER)
                .count();
    }

}
