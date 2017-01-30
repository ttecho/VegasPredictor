package com.github.techo.service;

import com.github.techo.entities.Prediction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    @Autowired
    MatchupService matchupService;

    @Autowired
    VegasService vegasService;

    public Prediction retrievePrediction(String teamUrl,
                                         String teamName,
                                         String opponentUrl,
                                         String opponentName,
                                         double line,
                                         double overUnder) {
        return new Prediction(
                matchupService.getMatchup(teamUrl, teamName, opponentUrl, opponentName),
                vegasService.calculateVegasProjectedScore(line, overUnder)
        );
    }
}
