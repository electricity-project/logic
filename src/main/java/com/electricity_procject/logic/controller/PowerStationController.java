package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.PowerStation;
import com.electricity_procject.logic.service.PowerStationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/power-station")
public class PowerStationController {
    private final PowerStationService powerStationService;

    public PowerStationController(PowerStationService powerStationService) {
        this.powerStationService = powerStationService;
    }

    @GetMapping
    public ResponseEntity<List<PowerStation>> getPowerStations(Pageable pageable) {
        return ResponseEntity.ok(powerStationService.getPowerStations(pageable));
    }

    @PostMapping
    public ResponseEntity<PowerStation> connectPowerStation(@RequestBody PowerStation powerStation) {
        return ResponseEntity.ok(powerStationService.connectPowerStation(powerStation));
    }
}
