package com.maemresen.fintractk.webservice.test.base;

import com.maemresen.fintractk.webservice.test.util.ContainerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractBasePostgresIT {

    protected static final PostgreSQLContainer<?> GLOBAL_POSTGRESQL_CONTAINER = ContainerFactory.createPostgreSQLContainer();

    static {
        GLOBAL_POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", GLOBAL_POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", GLOBAL_POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", GLOBAL_POSTGRESQL_CONTAINER::getPassword);
    }
}
