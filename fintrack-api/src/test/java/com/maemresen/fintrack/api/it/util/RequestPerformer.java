package com.maemresen.fintrack.api.it.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.util.HeaderConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class RequestPerformer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private <T> T readResponse(ResultActions resultActions, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);
    }

    public ResultActions perform(RequestConfig requestConfig) throws Exception {
        final var requestMethod = requestConfig.getRequestMethod();
        final var requestUri = requestConfig.getRequestUri();
        final var requestVariables = requestConfig.getRequestVariables().toArray();
        var requestBuilder = MockMvcRequestBuilders.request(requestMethod, requestUri, requestVariables)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        if (requestConfig.getRequestBody() != null) {
            requestBuilder.content(objectMapper.writeValueAsString(requestConfig.getRequestBody()));
        }

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        var httpStatus = Optional.ofNullable(requestConfig.getResponseHttpStatus());
        if (httpStatus.isPresent()) {
            resultActions.andExpect(status().is(httpStatus.get().value()));
        }

        var responseExceptionType = requestConfig.getResponseExceptionType();
        if (responseExceptionType != null) {
            resultActions.andExpect(header().string(HeaderConstants.ERROR_CODE_HEADER, responseExceptionType.getCode()));
        }
        return resultActions;
    }

    public <T> T perform(RequestConfig requestConfig, TypeReference<T> typeReference) throws Exception {
        final var performResultActions = perform(requestConfig);
        performResultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        return readResponse(performResultActions, typeReference);
    }
}
