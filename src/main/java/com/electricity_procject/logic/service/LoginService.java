package com.electricity_procject.logic.service;

import com.electricity_procject.logic.config.KeycloakConfig;
import com.electricity_procject.logic.domain.LoginRequest;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LoginService {
    private String baseUrl = KeycloakConfig.serverUrl;
    private String clientid = "szoze-client";
    private String clientSecret = "RANRxJtURJ0V6OvLJHQSgM9nbpqN7yRo";

    public String login(LoginRequest loginRequest) {
        WebClient webClient = WebClient.create(baseUrl);
        String endPointUrl = baseUrl + "/realms/szoze-realm/protocol/openid-connect/token";
        BodyInserters.FormInserter<String> requestBody = BodyInserters
                .fromFormData("grant_type", "password")
                .with("client_id", clientid)
                .with("client_secret", clientSecret)
                .with("username", loginRequest.username())
                .with("password", loginRequest.password());
        return webClient.post()
                .uri(endPointUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
