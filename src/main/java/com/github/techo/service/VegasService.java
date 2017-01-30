package com.github.techo.service;

import com.github.techo.entities.Game;
import com.github.techo.entities.Line;
import com.github.techo.entities.LineEnum;
import com.github.techo.entities.OverUnder;
import com.github.techo.entities.OverUnderEnum;
import com.github.techo.entities.VegasProjection;
import com.github.techo.entities.VegasResult;
import com.github.techo.entities.VegasStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VegasService {

    @Autowired
    private SpreadService spreadService;

    @Autowired
    private OverUnderService overUnderService;

    public VegasProjection calculateVegasProjectedScore(double line, double overUnder) {
        return VegasProjection.builder()
                .line(line)
                .overUnder(overUnder)
                .points((overUnder / 2) - (line / 2))
                .opponentPoints((overUnder / 2) + (line / 2))
                .build();
    }

    VegasStats getVegasStats(List<Game> playedGames) {
        return VegasStats.builder()
                .spread(spreadService.getSpreadStats(playedGames))
                .overUnder(overUnderService.getOverUnderStats(playedGames))
                .build();
    }

    VegasResult calculateVegasResult(VegasProjection projection, int points, int opponenetPoints) {
        return VegasResult.builder()
                .overUnder(getOverUnder(projection, points, opponenetPoints))
                .line(getLine(projection, points, opponenetPoints))
                .build();
    }

    private Line getLine(VegasProjection projection, int points, int opponenetPoints) {
        return Line.builder()
                .result(getLineResult(points, opponenetPoints, projection.getLine()))
                .actual(opponenetPoints - points)
                .difference(projection.getLine() - (opponenetPoints - points))
                .build();
    }

    private OverUnder getOverUnder(VegasProjection projection, int points, int opponenetPoints) {
        return OverUnder.builder()
                .result(getOverUnderResult(points, opponenetPoints, projection.getOverUnder()))
                .actual(points + opponenetPoints)
                .difference(projection.getOverUnder() - (points + opponenetPoints))
                .build();
    }

    private LineEnum getLineResult(int points, int opposingTeamPoints, double line) {
        return (points - opposingTeamPoints + line) > 0 ? LineEnum.COVER :
                ((points - opposingTeamPoints + line) == 0 ? LineEnum.PUSH :
                        LineEnum.DID_NOT_COVER);
    }

    private OverUnderEnum getOverUnderResult(int points, int opposingTeamPoints, double totalForOverUnder) {
        return (points + opposingTeamPoints) > totalForOverUnder ? OverUnderEnum.OVER :
                (points + opposingTeamPoints == totalForOverUnder ?OverUnderEnum.PUSH :
                        OverUnderEnum.UNDER);
    }
}
