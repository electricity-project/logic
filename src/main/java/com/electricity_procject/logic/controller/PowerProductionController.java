package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.PowerProduction;
import com.electricity_procject.logic.service.AggregationPeriodType;
import com.electricity_procject.logic.service.PowerProductionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/power-production")
public class PowerProductionController {
    private final PowerProductionService powerProductionService;

    public PowerProductionController(PowerProductionService powerProductionService) {
        this.powerProductionService = powerProductionService;
    }

    @GetMapping
    public ResponseEntity<List<PowerProduction>> getPowerProduction(@RequestParam String ipv6,
                                                                    @RequestParam Integer duration,
                                                                    @RequestParam AggregationPeriodType aggregationPeriodType,
                                                                    Pageable pageable) {
        return ResponseEntity.ok(powerProductionService.getPowerProduction(
                ipv6,
                duration,
                aggregationPeriodType,
                pageable));
    }
}