package com.maemresen.fintrack.api.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
public class ApiRequestPerformHelper {
    private final MockMvc mockMvc;

    public ResultActions performSuccessfulApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return this.performSuccessfulApiCall(requestBuilder, true);
    }

    public ResultActions performSuccessfulApiCall(MockHttpServletRequestBuilder requestBuilder, boolean expectResponse) throws Exception {
        var resultActions = performApiCall(requestBuilder).andExpect(status().isOk());

        if (expectResponse) {
            resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        return resultActions;
    }

    public void performBadRequestApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        performApiCall(requestBuilder).andExpect(status().isBadRequest());
    }

    public void  performInternalServerErrorRequestApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        performApiCall(requestBuilder).andExpect(status().isInternalServerError());
    }


    public ResultActions performApiCall(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }
}
