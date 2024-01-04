package com.electricity_procject.logic.domain;

public record UserUpdateRequest(
        String email,
        String username,
        String password
) {
}
