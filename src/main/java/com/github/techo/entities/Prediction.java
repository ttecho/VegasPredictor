package com.github.techo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Prediction {
    private Matchup matchup;
    private VegasProjection vegasProjection;
}
