package com.maemresen.fintrack.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack")
@EnableJpaRepositories(basePackages = "com.maemresen.fintrack")
@EntityScan(basePackages = "com.maemresen.fintrack")
public class FintrackApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FintrackApiApplication.class, args);
    }
}
