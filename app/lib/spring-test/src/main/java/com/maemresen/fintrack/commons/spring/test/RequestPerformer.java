package com.maemresen.fintrack.commons.spring.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class RequestPerformer {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ResultActions performRequest(final ApiRequest apiRequest) throws Exception {
        Objects.requireNonNull(apiRequest);
        final var requestMethod = apiRequest.getRequestMethod();
        final var requestUri = apiRequest.getRequestUri();
        final var requestVariables = apiRequest.getRequestVariables().toArray();
        var requestBuilder = MockMvcRequestBuilders.request(requestMethod, requestUri, requestVariables)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        if (apiRequest.getRequestBody() != null) {
            requestBuilder.content(objectMapper.writeValueAsString(apiRequest.getRequestBody()));
        }

        return mockMvc.perform(requestBuilder);
    }

    public void assertResultStatusAndHeaders(final ResultActions resultActions, final ApiResponse apiResponse) throws Exception {
        final var httpStatusCode = Optional.ofNullable(apiResponse.getHttpStatusCode());
        if (httpStatusCode.isPresent()) {
            resultActions.andExpect(status().is(httpStatusCode.get()));
        }

        final Map<String, Object> headers = Objects.requireNonNull(apiResponse.getHeaders());
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            final String header = entry.getKey();
            final Object value = entry.getValue();
            final String valueStr = Objects.toString(value, null);
            resultActions.andExpect(header().string(header, valueStr));
        }
    }

//    public  <T> T readResponse(final ResultActions resultActions, final TypeReference<T> typeReference) throws IOException {
//        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);
//    }
}
