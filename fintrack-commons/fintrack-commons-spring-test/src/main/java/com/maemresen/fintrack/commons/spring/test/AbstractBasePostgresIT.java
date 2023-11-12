package com.maemresen.fintrack.commons.spring.test;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ContextConfiguration(initializers = AbstractBasePostgresIT.ContextInitializer.class)
public abstract class AbstractBasePostgresIT {

    protected static final ContainerHolder<PostgreSQLContainer<?>> GLOBAL_POSTGRESQL_CONTAINER = ContainerHolder
        .<PostgreSQLContainer<?>>builder()
        .name("GlobalPostgreSQL")
        .container(ContainerFactory.createPostgreSQLContainer())
        .build();

    @Slf4j
    static class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

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
