package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.PowerProductionAggregation;
import com.electricity_procject.logic.service.AggregationPeriodType;
import com.electricity_procject.logic.service.PowerProductionAggregationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aggregated-power-production")
public class PowerProductionAggregationController {
    private final PowerProductionAggregationService powerProductionAggregationService;

    public PowerProductionAggregationController(PowerProductionAggregationService powerProductionAggregationService) {
        this.powerProductionAggregationService = powerProductionAggregationService;
    }

    @GetMapping
    public ResponseEntity<List<PowerProductionAggregation>> getPowerStations(
            @RequestParam AggregationPeriodType aggregationPeriodType,
            @RequestParam Integer duration
    ) {
        return ResponseEntity.ok(powerProductionAggregationService.getAggregatedPowerProduction(
                aggregationPeriodType, duration
        ));
    }
}
