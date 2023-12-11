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
public class PowerStation {
    private Long id;
    private String ipv6;
    private String state;
    private LocalDateTime creationTime;
    private double maxPower;
}
