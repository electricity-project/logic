package com.electricity_procject.logic.domain;

import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public record User(
        String id,
        String username,
        List<RoleRepresentation> roles
) {
}
