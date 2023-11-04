package com.maemresen.fintrack.commons.spring.test.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class ContainerFactory {

    public static PostgreSQLContainer<?> createPostgreSQLContainer() {
        try (var container = new PostgreSQLContainer<>("postgres:13.3")) {
            container.withDatabaseName("integration-tests-db");
            container.withUsername("sa");
            container.withPassword("sa");
            return container;
        }
    }
}
