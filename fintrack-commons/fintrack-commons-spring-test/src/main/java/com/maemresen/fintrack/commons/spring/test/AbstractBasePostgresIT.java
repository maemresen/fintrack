package com.maemresen.fintrack.commons.spring.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBasePostgresIT {

    @Container
    protected static final PostgreSQLContainer<?> GLOBAL_POSTGRESQL_CONTAINER = ContainerFactory.createPostgreSQLContainer();

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", GLOBAL_POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", GLOBAL_POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", GLOBAL_POSTGRESQL_CONTAINER::getPassword);
    }
}
