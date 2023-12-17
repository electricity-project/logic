package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerStationService {
    @Value("${api.base-url}")
    private String baseUrl;
    public List<PowerStation> getPowerStations(Pageable pageable) {
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
                .collectList()
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
}
