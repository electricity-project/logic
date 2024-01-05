package com.electricity_procject.logic.domain;

public record UserRequest(
        String username,
        UserRole role
) {}
