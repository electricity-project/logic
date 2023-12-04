package com.electricity_procject.logic.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PowerProduction {
    private Long id;
    private Long producedPower;
    private String ipv6;
    private PowerStationState state;
    private LocalDateTime timestamp;

}
