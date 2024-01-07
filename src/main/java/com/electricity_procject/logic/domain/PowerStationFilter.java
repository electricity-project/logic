package com.electricity_procject.logic.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PowerStationFilter {
    private Set<String> ipv6Patterns;
    private Set<PowerStationState> statePatterns;
    private Set<PowerStationType> typePatterns;
}
