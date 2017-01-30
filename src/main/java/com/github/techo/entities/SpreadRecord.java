package com.github.techo.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpreadRecord {
    private long cover;
    private long push;
    private long loss;
}
