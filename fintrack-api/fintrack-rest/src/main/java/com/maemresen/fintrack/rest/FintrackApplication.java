package com.maemresen.fintrack.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.maemresen.fintrack")
@EnableJpaRepositories(basePackages = "com.maemresen.fintrack")
@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack")
public class FintrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintrackApplication.class, args);
    }
}

