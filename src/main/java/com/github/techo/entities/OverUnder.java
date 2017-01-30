package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverUnder {
    private OverUnderEnum result;
    private int actual;
    private double difference;
}
