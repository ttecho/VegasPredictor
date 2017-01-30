package com.github.techo.util;

import java.util.List;

public class Calculation {
    public static double calculateAverage(List<Integer> actuals) {
        return actuals.stream()
                .mapToDouble(actual -> actual)
                .average()
                .getAsDouble();
    }
}
