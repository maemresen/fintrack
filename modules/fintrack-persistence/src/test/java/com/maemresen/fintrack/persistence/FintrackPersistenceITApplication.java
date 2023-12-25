package com.maemresen.fintrack.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.maemresen.fintrack")
@EnableJpaRepositories(basePackages = "com.maemresen.fintrack")
@EntityScan(basePackages = "com.maemresen.fintrack")

public class FintrackPersistenceITApplication {
}
