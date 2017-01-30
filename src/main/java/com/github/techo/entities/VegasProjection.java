package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VegasProjection {
    private double line;
    private double overUnder;
    private double points;
    private double opponentPoints;
}
