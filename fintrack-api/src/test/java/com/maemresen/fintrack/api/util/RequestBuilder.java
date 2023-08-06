package com.maemresen.fintrack.api.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestBuilder {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static final class WithoutBodyBuilder extends AbstractBaseRequestBuilder {
        private HttpMethod method;
        private String uri;

        @Singular
        private List<Object> variables;

        public MockHttpServletRequestBuilder build() {
            return request(method, uri, variables.toArray())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static final class WithBodyBuilder<T> extends AbstractBaseRequestBuilder {
        private final ObjectMapper objectMapper;
        private HttpMethod method;
        private String uri;
        private List<Object> variables;
        private T body;

        public MockHttpServletRequestBuilder build() throws IOException {
            return request(method, uri, CollectionUtils.emptyIfNull(variables).toArray())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(writeValueAsByte(body));
        }

        private String writeValueAsByte(Object content) throws IOException {
            return objectMapper.writeValueAsString(content);
        }
    }

    public static WithoutBodyBuilder withoutBody() {
        return new WithoutBodyBuilder();
    }

    public static <T> WithBodyBuilder<T> withBody(@NotNull ObjectMapper objectMapper) {
        return new WithBodyBuilder<T>(objectMapper);
    }
}
