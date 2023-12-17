package com.electricity_procject.logic.service;

import com.electricity_procject.logic.domain.PowerProductionAggregation;
import com.electricity_procject.logic.domain.PowerStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PowerProductionAggregationService {
    @Value("${api.base-url}")
    private String baseUrl;
    public List<PowerProductionAggregation> getAggregatedPowerProduction(AggregationPeriodType aggregationPeriodType,
                                                                         Integer duration) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculations-db-access/aggregated-power-production/" + aggregationPeriodType.toString()
                + "/" + duration.toString();
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .build())
                .retrieve()
                .bodyToFlux(PowerProductionAggregation.class)
                .collectList()
                .block();
    }
}
