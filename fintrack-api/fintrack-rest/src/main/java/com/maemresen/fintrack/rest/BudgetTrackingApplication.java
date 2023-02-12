package com.maemresen.fintrack.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.maemresen.bt")
@EnableJpaRepositories(basePackages = "com.maemresen.bt")
@SpringBootApplication(scanBasePackages = "com.maemresen.bt")
public class BudgetTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetTrackingApplication.class, args);
    }
}

