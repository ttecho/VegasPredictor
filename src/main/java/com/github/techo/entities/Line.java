package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Line {
    private LineEnum result;
    private int actual;
    private double difference;
}
