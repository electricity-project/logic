package com.electricity_procject.logic.controller;

import com.electricity_procject.logic.domain.User;
import com.electricity_procject.logic.domain.UserRequest;
import com.electricity_procject.logic.domain.UserResponse;
import com.electricity_procject.logic.service.UserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/info/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest request,
                                                   @RequestParam String userId) {
        UserResponse userResponse = userService.updateUser(request, userId);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/my-password")
    public ResponseEntity<UserResponse> resetMyPassword(@RequestParam String password) {
        UserResponse userResponse = userService.resetMyPassword(password);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/password")
    public ResponseEntity<UserResponse> resetPassword(@RequestParam String userId) {
        UserResponse userResponse = userService.resetPassword(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.validateUsername(username));
    }

    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class, ClientErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
