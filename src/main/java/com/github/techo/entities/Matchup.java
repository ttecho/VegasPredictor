package com.github.techo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Matchup {
    private Team team;
    private Team opponent;
}
