package com.maemresen.fintrack.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack")
public class FintrackApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FintrackApiApplication.class, args);
    }
}
