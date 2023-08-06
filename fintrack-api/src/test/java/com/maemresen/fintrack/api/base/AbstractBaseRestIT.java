package com.maemresen.fintrack.api.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.config.TestJacksonConfig;
import com.maemresen.fintrack.api.util.ApiRequestPerformHelper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestJacksonConfig.class)
@ActiveProfiles({"it"})
public abstract class AbstractBaseRestIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // Inject the custom ObjectMapper

    protected ApiRequestPerformHelper apiRequestPerformHelper;

    @BeforeEach
    void setUp() {
        apiRequestPerformHelper = new ApiRequestPerformHelper(mockMvc);
    }

    protected <T> T readResponse(MockHttpServletResponse response, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(response.getContentAsString(), typeReference);
    }
}