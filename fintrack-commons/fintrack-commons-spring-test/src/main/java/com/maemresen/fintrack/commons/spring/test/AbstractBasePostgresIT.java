package com.maemresen.fintrack.commons.spring.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(initializers = AbstractBasePostgresIT.ContextInitializer.class)
@ActiveProfiles("it")
public abstract class AbstractBasePostgresIT {

    public static final ContainerHolder<PostgreSQLContainer<?>> GLOBAL_POSTGRESQL_CONTAINER = ContainerHolder
        .<PostgreSQLContainer<?>>builder()
        .name("GlobalPostgreSQL")
        .container(ContainerFactory.createPostgreSQLContainer())
        .build();

    public static class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            GLOBAL_POSTGRESQL_CONTAINER.restart();
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        final var container = GLOBAL_POSTGRESQL_CONTAINER.getContainer();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
