package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerProduction;
import com.electricity_procject.logic.domain.PowerStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerProductionService {
    @Value("${api.base-url}")
    private String baseUrl;

    public List<PowerProduction> getPowerProduction(String ipv6, Pageable pageable) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/power-production/" + ipv6 ;
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .queryParam("page", pageable.getPageNumber())
                        .queryParam("size", pageable.getPageSize())
                        .build())
                .retrieve()
                .bodyToFlux(PowerProduction.class)
                .collectList()
                .block();
    }
}
