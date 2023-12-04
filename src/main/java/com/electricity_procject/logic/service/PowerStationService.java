package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerStationService {
    @Value("${api.base-url}")
    private String baseUrl;
    public List<PowerStation> getPowerStations() {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = baseUrl + "/calculations-db-access/power-station";
        return webClient
                .get()
                .uri(endpointUrl)
                .retrieve()
                .bodyToFlux(PowerStation.class)
                .collectList()
                .block();
    }
}
