package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.PowerStation;
import com.electricity_procject.logic.service.PowerStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/power-station")
public class PowerStationController {
    private final PowerStationService powerStationService;

    public PowerStationController(PowerStationService powerStationService) {
        this.powerStationService = powerStationService;
    }

    @GetMapping
    public ResponseEntity<List<PowerStation>> getPowerStations() {
        return ResponseEntity.ok(powerStationService.getPowerStations());
    }
}
