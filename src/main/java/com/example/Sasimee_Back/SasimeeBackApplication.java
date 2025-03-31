package com.example.Sasimee_Back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SasimeeBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SasimeeBackApplication.class, args);
	}

}
