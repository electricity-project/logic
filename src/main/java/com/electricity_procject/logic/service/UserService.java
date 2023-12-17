package com.electricity_procject.logic.service;

import com.electricity_procject.logic.config.Credentials;
import com.electricity_procject.logic.config.KeycloakConfig;
import com.electricity_procject.logic.domain.UserRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService {
    @Value("${keycloak.szoze-realm}")
    String szozeRealm;

    public UserRepresentation addUser(UserRequest userRequest) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(String.valueOf(UUID.randomUUID()));
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();

        Response savedUser = usersResource.create(user);

        RoleRepresentation userRole = realmResource.roles().get("USER").toRepresentation();

        String userId = CreatedResponseUtil.getCreatedId(savedUser);
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(userRole));
        return user;
    }
}
