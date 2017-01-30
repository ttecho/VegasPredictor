package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpreadStats {
    private Average averages;
    private SpreadRecord record;
    private List<Integer> all;
}
