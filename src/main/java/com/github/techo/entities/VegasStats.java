package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VegasStats {
    private SpreadStats spread;
    private OverUnderStats overUnder;
}
