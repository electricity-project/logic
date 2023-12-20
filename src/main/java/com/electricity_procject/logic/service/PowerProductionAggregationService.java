package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerProductionAggregation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerProductionAggregationService {
    @Value("${api.base.url}")
    private String baseUrl;

    public List<PowerProductionAggregation> getAggregatedPowerProduction(AggregationPeriodType aggregationPeriodType,
                                                                         Integer duration) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/aggregated-power-production";
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .queryParam("periodType", aggregationPeriodType.toString())
                        .queryParam("duration", duration)
                        .build())
                .retrieve()
                .bodyToFlux(PowerProductionAggregation.class)
                .collectList()
                .block();
    }
}
