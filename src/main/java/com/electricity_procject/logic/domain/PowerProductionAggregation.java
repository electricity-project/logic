package com.electricity_procject.logic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PowerProductionAggregation {
    private Long aggregatedValue;
    private Long powerStations;
    private LocalDateTime timestamp;
}
