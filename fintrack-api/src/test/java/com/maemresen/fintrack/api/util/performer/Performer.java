package com.maemresen.fintrack.api.util.performer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.utils.constants.HeaderConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class Performer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private <T> T readResponse(MockHttpServletResponse response, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(response.getContentAsString(), typeReference);
    }

    public ResultActions perform(RequestConfig requestConfig) throws Exception {
        var requestBuilder = request(requestConfig.getRequestMethod(), requestConfig.getRequestUri(), requestConfig.getRequestVariables().toArray())
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

        if (requestConfig.isExpectResponseBody()) {
            resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        } else {
            resultActions.andExpect(MockMvcResultMatchers.content().string(""));
        }

        return resultActions;
    }

    public <T> T get(RequestConfig requestConfig, TypeReference<T> typeReference) throws Exception {
        var resultActions = perform(requestConfig);
        return readResponse(resultActions.andReturn().getResponse(), typeReference);
    }
}
