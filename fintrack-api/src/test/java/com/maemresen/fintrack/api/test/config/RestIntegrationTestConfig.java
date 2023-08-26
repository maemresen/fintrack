package com.maemresen.fintrack.api.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maemresen.fintrack.api.test.util.container.PostgreSQLContainerManager;
import com.maemresen.fintrack.api.test.util.data.loader.BudgetListDataLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RestIntegrationTestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public BudgetListDataLoader dataLoader() {
        return new BudgetListDataLoader();
    }

    @Bean
    public PostgreSQLContainerManager postgreSQLContainerManager() {
        return new PostgreSQLContainerManager();
    }
}
