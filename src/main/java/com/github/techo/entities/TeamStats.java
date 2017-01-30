package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamStats {
    private Scores scores;
    private VegasStats vegas;
}
