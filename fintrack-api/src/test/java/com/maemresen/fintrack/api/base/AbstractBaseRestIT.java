package com.maemresen.fintrack.api.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.config.TestJacksonConfig;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.HeaderConstants;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResultActions perform(RequestConfig requestConfig) throws Exception {

        var requestBuilder = request(requestConfig.getRequestMethod(), requestConfig.getRequestUri(), requestConfig.getRequestVariables().toArray())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        if(requestConfig.getRequestBody() != null){
            requestBuilder.content(objectMapper.writeValueAsString(requestConfig.getRequestBody()));
        }

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        if (requestConfig.isExpectResponseBody()) {
            resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }

        var httpStatus = Optional.ofNullable(requestConfig.getResponseHttpStatus());
        if (httpStatus.isPresent()) {
            resultActions.andExpect(status().is(httpStatus.get().value()));
        }

        var responseExceptionType = requestConfig.getResponseExceptionType();
        if(responseExceptionType != null){
            resultActions.andExpect(header().string(HeaderConstants.ERROR_CODE_HEADER, responseExceptionType.getCode()));
        }

        return resultActions;
    }

    public <T> T performAndReturn(RequestConfig requestConfig, TypeReference<T> typeReference) throws Exception {
        var resultActions = perform(requestConfig);
        return readResponse(resultActions.andReturn().getResponse(), typeReference);
    }
}