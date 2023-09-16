package com.maemresen.fintrack.webservice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack.webservice")
@EnableJpaRepositories(basePackages = "com.maemresen.fintrack.webservice")
@EntityScan(basePackages = "com.maemresen.fintrack.webservice")
public class FintrackApplication {
    public static void main(String[] args) {
        SpringApplication.run(FintrackApplication.class, args);
    }
}
