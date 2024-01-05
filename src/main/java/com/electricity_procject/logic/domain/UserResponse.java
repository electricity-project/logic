package com.electricity_procject.logic.domain;

import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public record UserResponse(
        String id,
        String username,
        String password,
        List<RoleRepresentation> roles
) {
}
