package com.electricity_procject.logic.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PowerProduction {
    private Long id;
    private String ipv6Address;
    private String state;
    private String power;
    private String timestamp;

}
