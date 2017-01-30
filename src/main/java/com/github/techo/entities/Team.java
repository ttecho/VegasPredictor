package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Team {
    private String teamName;
    private TeamStats stats;
    private List<Game> games;
}
