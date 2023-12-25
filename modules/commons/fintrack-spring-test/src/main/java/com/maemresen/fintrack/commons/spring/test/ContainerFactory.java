package com.maemresen.fintrack.commons.spring.test;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class ContainerFactory {

    public static <T extends PostgreSQLContainer<T>> PostgreSQLContainer<T> createPostgreSQLContainer() {
        try (var container = new PostgreSQLContainer<T>("postgres:13.3")) {
            container.withDatabaseName("integration-tests-db");
            container.withUsername("sa");
            container.withPassword("sa");
            return container;
        }
    }
}
