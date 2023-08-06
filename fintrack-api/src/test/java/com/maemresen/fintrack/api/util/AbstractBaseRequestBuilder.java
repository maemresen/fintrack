package com.maemresen.fintrack.api.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(fluent = true, chain = true)
public abstract class AbstractBaseRequestBuilder {

    private HttpMethod method;
    private String uri;

    @Singular
    private List<Object> variables;

    public MockHttpServletRequestBuilder build() throws IOException {
        return request(method, uri, variables.toArray())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}
