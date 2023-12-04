package com.electricity_procject.logic.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class PowerStation {
    private Long id;
    private String ipv6Address;
    private PowerStationState state;
    private LocalDateTime creationTime;
    private double maxPower;
    private boolean isConnected;
    private Set<PowerProduction> powerProductions = new LinkedHashSet<>();
}
