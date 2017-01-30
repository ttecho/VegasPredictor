package com.github.techo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Average {
    private double prediction;
    private double actual;
    private double difference;
}
