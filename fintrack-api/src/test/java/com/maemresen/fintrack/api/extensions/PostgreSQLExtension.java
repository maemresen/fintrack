package com.maemresen.fintrack.api.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgreSQLExtension implements BeforeAllCallback, AfterAllCallback {

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("Starting PostgreSQL container...");
        POSTGRESQL_CONTAINER.start();
        log.info("PostgreSQL container started. Setting system properties...");
        System.setProperty("spring.datasource.url", POSTGRESQL_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRESQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRESQL_CONTAINER.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.info("Stopping PostgreSQL container...");
        POSTGRESQL_CONTAINER.stop();
        log.info("PostgreSQL container stopped.");
    }
}






