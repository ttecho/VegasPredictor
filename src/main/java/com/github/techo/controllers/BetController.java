package com.github.techo.controllers;

import com.github.techo.entities.Game;
import com.github.techo.entities.Prediction;
import com.github.techo.service.GamesService;
import com.github.techo.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bet")
public class BetController {
    public String KANSAS_BASKETBALL_URL = "http://www.vegasinsider.com/college-basketball/teams/team-page.cfm/team/kansas";
    public String KANSAS_BASKETBALL_TEAM_NAME = "Kansas Jayhawks";
    public String KENTUCKY_BASKETBALL_URL = "http://www.vegasinsider.com/college-basketball/teams/team-page.cfm/team/kentucky";
    public String KENTUCKY_BASKETBALL_TEAM_NAME = "Kentucky Wildcats";
    public double LINE = 7;
    public double OVER_UNDER = 168;

    @Autowired
    GamesService gamesService;

    @Autowired
    PredictionService predictionService;

    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public List<Game> getWins() throws IOException {
        return gamesService.retrieveGameResults(KANSAS_BASKETBALL_URL, KANSAS_BASKETBALL_TEAM_NAME, true);
    }

    @RequestMapping(path = "/prediction", method = RequestMethod.GET)
    public Prediction getPredictions() throws IOException {
        return predictionService.retrievePrediction(
                KANSAS_BASKETBALL_URL,
                KANSAS_BASKETBALL_TEAM_NAME,
                KENTUCKY_BASKETBALL_URL,
                KENTUCKY_BASKETBALL_TEAM_NAME,
                LINE,
                OVER_UNDER);
    }
}
