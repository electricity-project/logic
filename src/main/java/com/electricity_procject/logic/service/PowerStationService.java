package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerStation;
import com.electricity_procject.logic.domain.PowerStationState;
import com.electricity_procject.logic.domain.PowerStationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PowerStationService {
    @Value("${api.base.url}")
    private String baseUrl;

    public Page<PowerStation> getPowerStations(Pageable pageable) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/power-station";
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToFlux(PowerStation.class)
                .map(powerStation -> {
                    if (powerStation.getBladeLength() != null) {
                        powerStation.setType(PowerStationType.WIND_TURBINE);
                    } else {
                        powerStation.setType(PowerStationType.SOLAR_PANEL);
                    }
                    return powerStation;
                })
                .collectList()
                .map(list -> new PageImpl<>(list, pageable, list.size()))
                .block();
    }

    public PowerStation connectPowerStation(PowerStation powerStation) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/power-station";
        return webClient
                .post()
                .uri(endpointUrl)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(powerStation))
                .retrieve()
                .bodyToMono(PowerStation.class)
                .block();
    }

    public Map<PowerStationState, Integer> getPowerStationCount() {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/power-station/count";
        return webClient.get()
                .uri(endpointUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::convertToPowerStationStateMap)
                .block();
    }

    private Map<PowerStationState, Integer> convertToPowerStationStateMap(Map<String, Integer> responseMap) {
        return responseMap.entrySet().stream()
                .collect(Collectors.toMap(
                                entry -> PowerStationState.valueOf(entry.getKey()),
                                Map.Entry::getValue
                        )
                );
    }

}
