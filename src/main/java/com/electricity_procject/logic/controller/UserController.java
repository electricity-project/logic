package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.UserRequest;
import com.electricity_procject.logic.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody UserRequest userRequest) {
        UserRepresentation userRepresentation = userService.addUser(userRequest);
        return ResponseEntity.ok(userRepresentation);
    }
}
