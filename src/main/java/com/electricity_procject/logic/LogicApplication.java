package com.electricity_procject.logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class LogicApplication {
	@RequestMapping("/")
	public String home(){
		return "Logic works";
	}

	public static void main(String[] args) {
		SpringApplication.run(LogicApplication.class, args);
	}

}
