package com.maemresen.fintrack.business;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack")
@EnableJpaRepositories(basePackages = "com.maemresen.fintrack")
@EntityScan(basePackages = "com.maemresen.fintrack")
public class FintrackBusinessITApplication {
}
