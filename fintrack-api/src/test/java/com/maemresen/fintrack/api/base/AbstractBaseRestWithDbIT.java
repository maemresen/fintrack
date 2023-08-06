package com.maemresen.fintrack.api.base;

import com.maemresen.fintrack.api.config.PostgresqlContainerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public abstract class AbstractBaseRestWithDbIT extends AbstractBaseRestIT {

    @BeforeAll
    static void beforeAll() {
        PostgresqlContainerConfig.start();
    }

    @AfterAll
    static void afterAll() {
        PostgresqlContainerConfig.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgresqlContainerConfig.configureProperties(registry);
    }
}
