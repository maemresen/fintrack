package com.maemresen.fintrack.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maemresen.fintrack.api.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.util.performer.BudgetRestItService;
import com.maemresen.fintrack.api.util.performer.Performer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class RestApiIntegrationTestConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public Performer performer(MockMvc mockMvc, ObjectMapper objectMapper) {
        return new Performer(mockMvc, objectMapper);
    }

    @Bean
    public BudgetRestItService budgetRestItService(Performer performer) {
        return new BudgetRestItService(performer);
    }

    @Bean
    public BudgetListDataLoader budgetListDataLoader() {
        return new BudgetListDataLoader();
    }
}
