package com.maemresen.fintrack.api.integration.test.base;

import com.maemresen.fintrack.api.integration.test.util.ContainerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
