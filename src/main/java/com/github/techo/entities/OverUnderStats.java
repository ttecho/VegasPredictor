package com.github.techo.entities;

import lombok.Data;

import java.util.List;

@Data
public class OverUnderStats {
    private Average averages;
    private OverUnderRecord record;
    private List<Integer> all;
}
