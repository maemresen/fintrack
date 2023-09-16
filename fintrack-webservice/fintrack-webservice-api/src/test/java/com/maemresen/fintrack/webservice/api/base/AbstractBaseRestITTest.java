package com.maemresen.fintrack.webservice.api.base;

import com.maemresen.fintrack.webservice.api.util.annotation.RestApiIT;
import com.maemresen.fintrack.webservice.api.util.helper.ContainerFactory;
import com.maemresen.fintrack.webservice.api.util.helper.RequestPerformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@RequiredArgsConstructor
@RestApiIT
public class AbstractBaseRestITTest {

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

    @Autowired
    protected RequestPerformer requestPerformer;
}
