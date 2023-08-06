package com.maemresen.fintrack.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

/**
 * Base abstract class to construct MockHttpServletRequestBuilder instances. Supports fluent-style API.
 *
 * <p>Provides two main builder types:
 * 1) WithoutBodyBuilder for requests without body.
 * 2) WithBodyBuilder for requests with body.
 *
 * @see WithoutBodyBuilder
 * @see WithBodyBuilder
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true, chain = true)
@Setter
@Getter(AccessLevel.PROTECTED)
public abstract class RequestBuilder {

    private HttpMethod method;
    private String uri;
    private List<Object> variables;
    private String x;

    /**
     * Constructs a MockHttpServletRequestBuilder with the set configurations.
     *
     * @return MockHttpServletRequestBuilder instance
     * @throws IOException if any IO error occurs
     */
    public MockHttpServletRequestBuilder build() throws IOException {
        return request(method(), uri(), CollectionUtils.emptyIfNull(variables()).toArray())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    /**
     * Builder subclass for constructing MockHttpServletRequestBuilder instances without a body.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static final class WithoutBodyBuilder extends RequestBuilder {
    }

    /**
     * Builder subclass for constructing MockHttpServletRequestBuilder instances with a body.
     */
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static final class WithBodyBuilder extends RequestBuilder {
        private final String body;

        /**
         * Overrides the build method of the parent to also set the body content.
         *
         * @return MockHttpServletRequestBuilder instance with body content
         * @throws IOException if any IO error occurs during content serialization
         */
        @Override
        public MockHttpServletRequestBuilder build() throws IOException {
            return super.build().content(body);
        }
    }

    /**
     * Factory method to instantiate a WithoutBodyBuilder.
     *
     * @return a new WithoutBodyBuilder instance
     */
    public static WithoutBodyBuilder withoutBody() {
        return new WithoutBodyBuilder();
    }

    /**
     * Factory method to instantiate a WithBodyBuilder.
     *
     * @param objectMapper the object mapper to use for serialization
     * @param body         the body content to set
     * @param <T>          type of the body content
     * @return a new WithBodyBuilder instance
     */
    public static <T> WithBodyBuilder withBody(@NotNull ObjectMapper objectMapper, T body) throws JsonProcessingException {
        String bodyJson = objectMapper.writeValueAsString(body);
        return new WithBodyBuilder(bodyJson);
    }
}
