package com.github.techo.service;

import com.github.techo.entities.Average;
import com.github.techo.entities.Game;
import com.github.techo.entities.Line;
import com.github.techo.entities.LineEnum;
import com.github.techo.entities.SpreadRecord;
import com.github.techo.entities.SpreadStats;
import com.github.techo.entities.Vegas;
import com.github.techo.entities.VegasProjection;
import com.github.techo.entities.VegasResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.techo.util.Calculation.calculateAverage;

@Service
class SpreadService {
    SpreadStats getSpreadStats(List<Game> playedGames) {
        return SpreadStats.builder()
                .averages(new Average(
                        calculateAverageSpread(playedGames),
                        calculateAverage(getActualsForSpread(playedGames)),
                        calculateAverageDifferenceFromSpread(playedGames)
                ))
                .record(getSpreadRecord(playedGames))
                .all(getActualsForSpread(playedGames))
                .build();
    }

    private List<Integer> getActualsForSpread(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getLine)
                .map(Line::getActual)
                .collect(Collectors.toList());
    }

    private double calculateAverageSpread(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getProjection)
                .mapToDouble(VegasProjection::getLine)
                .average()
                .getAsDouble();
    }

    private double calculateAverageDifferenceFromSpread(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getLine)
                .map(Line::getDifference)
                .mapToDouble(x -> x)
                .average()
                .getAsDouble();
    }

    private SpreadRecord getSpreadRecord(List<Game> playedGames) {
        return SpreadRecord.builder()
                .cover(calculateSpreadCovers(playedGames))
                .push(calculateSpreadPushes(playedGames))
                .loss(calculateSpreadLosses(playedGames))
                .build();
    }

    private long calculateSpreadCovers(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getLine)
                .filter(result -> result.getResult() == LineEnum.COVER)
                .count();
    }

    private long calculateSpreadPushes(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getLine)
                .filter(result -> result.getResult() == LineEnum.PUSH)
                .count();
    }

    private long calculateSpreadLosses(List<Game> playedGames) {
        return playedGames
                .stream()
                .map(Game::getVegas)
                .map(Vegas::getResult)
                .map(VegasResult::getLine)
                .filter(result -> result.getResult() == LineEnum.DID_NOT_COVER)
                .count();
    }
}
