package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.PowerStation;
import com.electricity_procject.logic.domain.PowerStationFilter;
import com.electricity_procject.logic.domain.PowerStationState;
import com.electricity_procject.logic.service.PowerStationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/power-station")
public class PowerStationController {
    private final PowerStationService powerStationService;

    public PowerStationController(PowerStationService powerStationService) {
        this.powerStationService = powerStationService;
    }

    @PostMapping
    public ResponseEntity<Page<PowerStation>> getPowerStations(@RequestBody PowerStationFilter powerStationFilter,
                                                               Pageable pageable) {
        return ResponseEntity.ok(powerStationService.getPowerStations(powerStationFilter, pageable));
    }

    @GetMapping("/connect")
    public ResponseEntity<Void> connectPowerStation(@RequestParam String ipv6) {
        powerStationService.connectPowerStation(ipv6);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disconnect")
    public ResponseEntity<Void> disconnectPowerStation(@RequestParam String ipv6) {
        powerStationService.disconnectPowerStation(ipv6);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/start")
    public ResponseEntity<Void> startPowerStation(@RequestParam String ipv6) {
        powerStationService.startPowerStation(ipv6);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stop")
    public ResponseEntity<Void> stopPowerStation(@RequestParam String ipv6) {
        powerStationService.stopPowerStation(ipv6);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<PowerStationState, Integer>> getPowerStationsCount() {
        return ResponseEntity.ok(powerStationService.getPowerStationCount());
    }
}
