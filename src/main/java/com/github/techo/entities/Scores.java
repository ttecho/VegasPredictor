package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Scores {
    private Points scored;
    private Points against;
}
