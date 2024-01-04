package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.UserRequest;
import com.electricity_procject.logic.domain.UserUpdateRequest;
import com.electricity_procject.logic.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserRepresentation> addUser(@RequestBody UserRequest userRequest) {
        UserRepresentation userRepresentation = userService.addUser(userRequest);
        return ResponseEntity.ok(userRepresentation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserRepresentation>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<UserRepresentation> updateUser(@RequestBody UserUpdateRequest request,
                                                         @RequestParam String userId) {
        UserRepresentation userRepresentation = userService.updateUser(request, userId);
        return ResponseEntity.ok(userRepresentation);
    }

    @PatchMapping("/password")
    public ResponseEntity<UserRepresentation> resetPassword(@RequestParam String userId,
                                                            @RequestParam String password) {
        UserRepresentation userRepresentation = userService.resetPassword(userId, password);
        return ResponseEntity.ok(userRepresentation);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
