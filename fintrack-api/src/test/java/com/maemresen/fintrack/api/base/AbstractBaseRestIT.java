package com.maemresen.fintrack.api.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.config.TestJacksonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestJacksonConfig.class)
@ActiveProfiles({"it"})
public abstract class AbstractBaseRestIT {

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

    protected ResultActions performSuccessfulApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return this.performSuccessfulApiCall(requestBuilder, true);
    }

    protected ResultActions performSuccessfulApiCall(MockHttpServletRequestBuilder requestBuilder, boolean expectResponse) throws Exception {
        var resultActions = performApiCall(requestBuilder).andExpect(status().isOk());

        if(expectResponse) {
            resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        return resultActions;
    }

    protected void performBadRequestApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        performApiCall(requestBuilder).andExpect(status().isBadRequest());
    }

    protected ResultActions performApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    protected MockHttpServletRequestBuilder httpRequest(HttpMethod method, String urlTemplate, Object... uriVariables) throws Exception {
        return request(method, urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    protected <T> MockHttpServletRequestBuilder httpRequestWihBody(HttpMethod method, String urlTemplate, T body, Object... uriVariables) throws Exception {
        return request(method, urlTemplate, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(writeValueAsByte(body));
    }
}