package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Points {
    private List<Integer> all;
    private double average;
}
