package com.maemresen.fintrack.api.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.commons.spring.test.AbstractBasePostgresIT;
import com.maemresen.fintrack.commons.spring.test.RequestPerformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = FintrackApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ContextConfiguration(classes = AbstractBaseRestIT.RestITConfig.class)
public abstract class AbstractBaseRestIT extends AbstractBasePostgresIT {
    @Autowired
    protected RequestPerformer requestPerformer;

    @TestConfiguration
    static class RestITConfig {

        @Bean
        public RequestPerformer requestPerformer(final MockMvc mockMvc){
            return new RequestPerformer(mockMvc, objectMapper());
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().registerModule(new JavaTimeModule());
        }
    }
}
