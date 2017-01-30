package com.github.techo.entities;

import lombok.Data;

import java.util.List;

@Data
public class Game {
    private String date;
    private String teamName;
    private String opponentName;
    private boolean homeTeam;
    private GameResult gameResult;
    private Vegas vegas;
    private List<Game> opponentGames;
    private String opponentUrl;
}
