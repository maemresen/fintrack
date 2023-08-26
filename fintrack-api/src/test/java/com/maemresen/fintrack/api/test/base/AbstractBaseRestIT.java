package com.maemresen.fintrack.api.test.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.test.config.RestIntegrationTestConfig;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.utils.constants.HeaderConstants;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = RestIntegrationTestConfig.class)
@ActiveProfiles({"it"})
public abstract class AbstractBaseRestIT {

    protected abstract MockMvc getMockMvc();

    protected abstract ObjectMapper getObjectMapper();

    protected <T> T readResponse(MockHttpServletResponse response, TypeReference<T> typeReference) throws IOException {
        return getObjectMapper().readValue(response.getContentAsString(), typeReference);
    }

    public ResultActions perform(RequestConfig requestConfig) throws Exception {

        var requestBuilder = request(requestConfig.getRequestMethod(), requestConfig.getRequestUri(), requestConfig.getRequestVariables().toArray())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if (requestConfig.getRequestBody() != null) {
            requestBuilder.content(getObjectMapper().writeValueAsString(requestConfig.getRequestBody()));
        }

        ResultActions resultActions = getMockMvc().perform(requestBuilder);

        var httpStatus = Optional.ofNullable(requestConfig.getResponseHttpStatus());
        if (httpStatus.isPresent()) {
            resultActions.andExpect(status().is(httpStatus.get().value()));
        }

        var responseExceptionType = requestConfig.getResponseExceptionType();
        if (responseExceptionType != null) {
            resultActions.andExpect(header().string(HeaderConstants.ERROR_CODE_HEADER, responseExceptionType.getCode()));
        }

        if (requestConfig.isExpectResponseBody()) {
            resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        } else {
            resultActions.andExpect(MockMvcResultMatchers.content().string(""));
        }

        return resultActions;
    }

    public <T> T performAndReturn(RequestConfig requestConfig, TypeReference<T> typeReference) throws Exception {
        var resultActions = perform(requestConfig);
        return readResponse(resultActions.andReturn().getResponse(), typeReference);
    }
}
