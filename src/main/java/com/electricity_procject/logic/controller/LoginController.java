package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.LoginRequest;
import com.electricity_procject.logic.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(loginService.refreshAccessToken(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        loginService.logout();
        return ResponseEntity.ok().build();
    }
}
