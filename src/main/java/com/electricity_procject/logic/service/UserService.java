package com.electricity_procject.logic.service;

import com.electricity_procject.logic.config.Credentials;
import com.electricity_procject.logic.config.KeycloakConfig;
import com.electricity_procject.logic.domain.UserRequest;
import com.electricity_procject.logic.domain.UserUpdateRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();

        Response savedUser = usersResource.create(user);

        RoleRepresentation userRole = realmResource.roles().get("USER_SET_PASSWORD").toRepresentation();

        if(savedUser.getStatus() == Response.Status.CREATED.getStatusCode()) {
            String userId = CreatedResponseUtil.getCreatedId(savedUser);
            UserResource userResource = usersResource.get(userId);
            userResource.roles().realmLevel().add(Collections.singletonList(userRole));
            return user;
        }
        throw new IllegalArgumentException("Could not add user");
    }

    public Page<UserRepresentation> getUsers(Pageable pageable) {
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> usersList =  usersResource.list();
        return new PageImpl<>(usersList, pageable, usersList.size());
    }

    public UserRepresentation updateUser(UserUpdateRequest request, String userId) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(request.password());
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);



        usersResource.get(userId).update(user);
        return user;

    }

    public UserRepresentation resetPassword(String userId, String password) {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(password);
        RealmResource realmResource = KeycloakConfig.getInstance().realm(szozeRealm);
        UsersResource usersResource = realmResource.users();
        RoleRepresentation userRole = realmResource.roles().get("USER").toRepresentation();
        UserResource userResource = usersResource.get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(userRole));
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        user.setCredentials(Collections.singletonList(credential));
        usersResource.get(userId).update(user);

        return user;
    }

    //TODO: validateUsername

    //TODO: weatherApiKey

    //TODO: setWeatherApiKey
}
