package com.maemresen.fintrack.api.integration.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maemresen.fintrack.api.integration.test.util.RequestPerformer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

@TestConfiguration
public class RestApiITConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public RequestPerformer performer(MockMvc mockMvc, ObjectMapper objectMapper) {
        return new RequestPerformer(mockMvc, objectMapper);
    }
}
