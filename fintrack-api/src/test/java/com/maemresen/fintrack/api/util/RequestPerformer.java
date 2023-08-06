package com.maemresen.fintrack.api.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Accessors(fluent = true, chain = true)
@Setter(AccessLevel.PRIVATE)
public class RequestPerformer {

    private MockMvc mockMvc;
    private MockHttpServletRequestBuilder requestBuilder;
    private boolean expectResponse;
    private HttpStatus httpStatus;

    public static Builder builder(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) {
        return new Builder(mockMvc, requestBuilder);
    }

    public static BadRequestBuilder badRequest(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) {
        return new Builder(mockMvc, requestBuilder)
                .expectResponse(false)
                .httpStatus(HttpStatus.BAD_REQUEST);
    }

    public static SuccessfulBuilder successful(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) {
        return new Builder(mockMvc, requestBuilder)
                .httpStatus(HttpStatus.OK);
    }


    public static SuccessfulBuilder error(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) {
        return new Builder(mockMvc, requestBuilder)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public interface BaseBuilder {
        RequestPerformer build() throws Exception;
    }

    public interface AllBuilder extends BaseBuilder {
        Builder expectResponse(boolean expectResponse);

        Builder httpStatus(HttpStatus httpStatus);

    }

    public interface BadRequestBuilder extends BaseBuilder {
        Builder expectResponse(boolean expectResponse);

    }

    public interface SuccessfulBuilder extends BaseBuilder {
        Builder expectResponse(boolean expectResponse);

    }

    public interface ErrorBuilder extends BaseBuilder {
        Builder expectResponse(boolean expectResponse);

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class Builder implements AllBuilder, BadRequestBuilder, SuccessfulBuilder, ErrorBuilder {
        private final MockMvc mockMvc;
        private final MockHttpServletRequestBuilder requestBuilder;
        private boolean expectResponse = true;
        private HttpStatus httpStatus;

        @Override
        public RequestPerformer build() throws Exception {
            return new RequestPerformer()
                    .mockMvc(mockMvc)
                    .requestBuilder(requestBuilder)
                    .expectResponse(expectResponse)
                    .httpStatus(httpStatus);
        }
    }

    public ResultActions perform() throws Exception {
        ResultActions resultActions = mockMvc.perform(requestBuilder);

        if (expectResponse) {
            resultActions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }

        if (httpStatus != null) {
            resultActions.andExpect(status().is(httpStatus.value()));
        }

        return resultActions;
    }
}




