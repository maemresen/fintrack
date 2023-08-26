package com.maemresen.fintrack.api.test.util.container;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class PostgreSQLContainerManager extends ContainerManager {

    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("Starting PostgreSQL container...");
        POSTGRESQL_CONTAINER.start();
        log.info("Setting system properties...");
        addSingleProperty(applicationContext, "spring.datasource.url", POSTGRESQL_CONTAINER.getJdbcUrl());
        addSingleProperty(applicationContext, "spring.datasource.username", POSTGRESQL_CONTAINER.getUsername());
        addSingleProperty(applicationContext, "spring.datasource.password", POSTGRESQL_CONTAINER.getPassword());
        log.info("System properties set.");
    }

    @Override
    public void cleanup() {
        log.info("Stopping PostgreSQL container...");
        POSTGRESQL_CONTAINER.stop();
        log.info("PostgreSQL container stopped.");
    }
}
