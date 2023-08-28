package com.maemresen.fintrack.api.test.util.helper;

import com.maemresen.fintrack.api.test.util.container.SpringBootPostgreSQLContainer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContainerFactory {

    public static SpringBootPostgreSQLContainer createPostgreSQLContainer() {
        try (SpringBootPostgreSQLContainer container = new SpringBootPostgreSQLContainer("postgres:13.3")) {
            container.withDatabaseName("integration-tests-db");
            container.withUsername("sa");
            container.withPassword("sa");
            return container;
        }
    }
}
