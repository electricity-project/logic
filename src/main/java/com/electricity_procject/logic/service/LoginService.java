package com.electricity_procject.logic.service;

import com.electricity_procject.logic.config.KeycloakConfig;
import com.electricity_procject.logic.domain.LoginRequest;
import jakarta.ws.rs.core.HttpHeaders;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LoginService {
    @Value("${keycloak.szoze-realm}")
    String szozeRealm;
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

    public String refreshAccessToken(String refreshToken) {
        WebClient webClient = WebClient.create(baseUrl);
        String endPointUrl = baseUrl + "/realms/szoze-realm/protocol/openid-connect/token";
        BodyInserters.FormInserter<String> requestBody = BodyInserters
                .fromFormData("grant_type", "refresh_token")
                .with("client_id", clientid)
                .with("client_secret", clientSecret)
                .with("refresh_token", refreshToken);
        return webClient.post()
                .uri(endPointUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void logout() {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String currentUserId = (String) jwt.getClaims().get("sub");
        usersResource.get(currentUserId).logout();
    }
}
