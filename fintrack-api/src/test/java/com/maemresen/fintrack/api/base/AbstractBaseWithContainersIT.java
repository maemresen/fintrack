package com.maemresen.fintrack.api.base;

import com.maemresen.fintrack.api.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.util.data.loader.Data;
import com.maemresen.fintrack.api.util.helper.ContainerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Data(sources = {
    @Data.Source(
        name = AbstractBaseWithContainersIT.BUDGET_DATA,
        path = "data/budgets.json",
        loader = BudgetListDataLoader.class
    )
})
public class AbstractBaseWithContainersIT {
    protected static final String BUDGET_DATA = "budgets";

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
