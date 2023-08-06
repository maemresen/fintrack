package com.maemresen.fintrack.api.it.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maemresen.fintrack.api.FintrackApiApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = AbstractBaseRestIT.TestConfig.class)
@ActiveProfiles({"it"})
public abstract class AbstractBaseRestIT {

    static PostgreSQLContainer<?> postgres;

    static {
         try (var container = new PostgreSQLContainer<>("postgres:15-alpine")){
             postgres = container;
         }
    }

    @Configuration
    static class TestConfig {
        // Create a custom ObjectMapper bean with JSR-310 module registered
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().registerModule(new JavaTimeModule());
        }
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // Inject the custom ObjectMapper

    protected <T> T readResponse(MockHttpServletResponse response, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(response.getContentAsString(), typeReference);
    }

    protected String writeValueAsByte(Object content) throws IOException {
        return objectMapper.writeValueAsString(content);
    }

    protected ResultActions performApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    protected ResultActions performSuccessfulApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return performApiCall(requestBuilder)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected void performBadRequestApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        performApiCall(requestBuilder).andExpect(status().isBadRequest());
    }
}