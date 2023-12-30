package com.electricity_procject.logic.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PowerStation {
    private Long id;
    @NonNull
    private String ipv6;
    @NonNull
    private String state;
    @NonNull
    private LocalDateTime creationTime;
    @NonNull
    private Double maxPower;
    private PowerStationType type;
    private double optimalTemperature;
    private Long bladeLength;
}
