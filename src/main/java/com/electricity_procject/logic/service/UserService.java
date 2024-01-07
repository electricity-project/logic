package com.electricity_procject.logic.service;

import com.electricity_procject.logic.config.Credentials;
import com.electricity_procject.logic.config.KeycloakConfig;
import com.electricity_procject.logic.domain.User;
import com.electricity_procject.logic.domain.UserRequest;
import com.electricity_procject.logic.domain.UserResponse;
import com.electricity_procject.logic.domain.WeatherApiKey;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Value("${keycloak.szoze-realm}")
    String szozeRealm;
    @Value("${api.base.url}")
    private String baseUrl;

    public UserResponse addUser(UserRequest userRequest) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(String.valueOf(UUID.randomUUID()));
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.username());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();

        Response savedUser = usersResource.create(user);

        List<RoleRepresentation> roles = new ArrayList<>();
        roles.add(realmResource.roles().get(userRequest.role().toString()).toRepresentation());
        roles.add(realmResource.roles().get("USER_SET_PASSWORD").toRepresentation());


        if(savedUser.getStatus() == Response.Status.CREATED.getStatusCode()) {
            String userId = CreatedResponseUtil.getCreatedId(savedUser);
            return getUserResponse(userId, usersResource, user, roles);
        }
        throw new IllegalArgumentException("Could not add user");
    }

    public Page<User> getUsers(Pageable pageable) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> userRepresentationList =  usersResource.list();
        List<User> userList = new ArrayList<>();
        for(UserRepresentation userRepresentation: userRepresentationList) {
            userList.add(new User(
                    userRepresentation.getId(),
                    userRepresentation.getUsername(),
                    usersResource.get(userRepresentation.getId()).roles().getAll().getRealmMappings()));
        }
        return new PageImpl<>(userList, pageable, userList.size());
    }

    public User getUser(String username) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> userRepresentationList =  usersResource.list();
        for (UserRepresentation userRepresentation: userRepresentationList) {
            if (userRepresentation.getUsername().equals(username)) {
                return new User(
                        userRepresentation.getId(),
                        userRepresentation.getUsername(),
                        usersResource.get(userRepresentation.getId()).roles().getAll().getRealmMappings());
            }
        }
        throw new NotFoundException("User with this username not found");
    }

    public User getCurrentUser() {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String currentUserId = (String) jwt.getClaims().get("sub");
        UserRepresentation userRepresentation = usersResource.get(currentUserId).toRepresentation();
        return new User(
                userRepresentation.getId(),
                userRepresentation.getUsername(),
                usersResource.get(userRepresentation.getId()).roles().getAll().getRealmMappings());
    }

    public UserResponse updateUser(UserRequest request, String userId) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setUsername(request.username());
        user.setEnabled(true);

        usersResource.get(userId).update(user);

        RoleRepresentation userRole = realmResource.roles().get(request.role().toString()).toRepresentation();

        return getUserResponse(userId, usersResource, user, Collections.singletonList(userRole));

    }

    private UserResponse getUserResponse(String userId,
                                         UsersResource usersResource,
                                         UserRepresentation user,
                                         List<RoleRepresentation> roles) {
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(roles);
        UserRepresentation savedUserRepresentation = usersResource.get(userId).toRepresentation();
        return new UserResponse(
                savedUserRepresentation.getId(),
                savedUserRepresentation.getUsername(),
                user.getCredentials().get(0).getValue(),
                usersResource.get(userId).roles().getAll().getRealmMappings());
    }

    public UserResponse resetPassword(String userId) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(String.valueOf(UUID.randomUUID()));
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        RoleRepresentation setPasswordRole = realmResource.roles().get("USER_SET_PASSWORD").toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(setPasswordRole));
        return getUserResponsePassword(userId, credential, usersResource);
    }

    public UserResponse resetMyPassword(String password) {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String userId = (String) jwt.getClaims().get("sub");
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(password);
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        RoleRepresentation oldRole = realmResource.roles().get("USER_SET_PASSWORD").toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().remove(Collections.singletonList(oldRole));
        return getUserResponsePassword(userId, credential, usersResource);
    }

    private UserResponse getUserResponsePassword(String userId, CredentialRepresentation credential, UsersResource usersResource) {
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setCredentials(Collections.singletonList(credential));
        usersResource.get(userId).update(user);

        UserRepresentation savedUserRepresentation = usersResource.get(userId).toRepresentation();
        return new UserResponse(
                savedUserRepresentation.getId(),
                savedUserRepresentation.getUsername(),
                user.getCredentials().get(0).getValue(),
                usersResource.get(userId).roles().getAll().getRealmMappings());
    }

    public void deleteUser(String userId) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        String currentUserId = (String) jwt.getClaims().get("sub");
        if (currentUserId.equals(userId)) {
            throw new IllegalArgumentException("Cannot delete current user");
        }
        usersResource.get(userId).remove();
    }

    public boolean validateUsername(String username) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> userRepresentationList =  usersResource.list();
        for (UserRepresentation userRepresentation: userRepresentationList) {
            if (userRepresentation.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void addWeatherApiKey(WeatherApiKey weatherApiKey) {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculation-db-access/weather-api";
        webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(weatherApiKey))
                .retrieve();
    }

    public WeatherApiKey getWeatherApiKey() {
        WebClient webClient = WebClient.create(baseUrl);
        String endpointUrl = "/calculation-db-access/weather-api";
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointUrl)
                        .build())
                .retrieve()
                .bodyToMono(WeatherApiKey.class)
                .block();
    }
}
