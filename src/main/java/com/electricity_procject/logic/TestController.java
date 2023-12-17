package com.electricity_procject.logic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/public/hello")
    ResponseEntity<String> helloMessage() {
        return ResponseEntity.ok("Hello! Here you don't have to authenticate!");
    }

    @GetMapping("/not-for-everyone")
    ResponseEntity<String> notForEveryone() {
        return ResponseEntity.ok("Only for you");
    }
}
