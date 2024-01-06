package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerProduction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerProductionService {
    @Value("${api.base.url}")
    private String baseUrl;

    public List<PowerProduction> getPowerProduction(String ipv6,
                                                    Integer duration,
                                                    AggregationPeriodType aggregationPeriodType) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/power-production/";
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .queryParam("ipv6", ipv6)
                        .queryParam("periodType", aggregationPeriodType.toString())
                        .queryParam("duration", duration)
                        .build())
                .retrieve()
                .bodyToFlux(PowerProduction.class)
                .collectList()
                .block();
    }


}
