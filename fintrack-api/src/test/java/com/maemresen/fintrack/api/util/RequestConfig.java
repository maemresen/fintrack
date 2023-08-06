package com.maemresen.fintrack.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public class RequestConfig {

    private HttpMethod method;
    private String uri;
    private List<Object> variables;
    private boolean bodyExists;
    private Object body;
    private boolean expectResponse;
    private HttpStatus httpStatus;
    
    public static WithoutBodyBuilder get(@NotBlank String uri) {
        return withoutBody(uri, HttpMethod.GET);
    }

    public static WithoutBodyBuilder delete(@NotBlank String uri) {
        return withoutBody(uri, HttpMethod.DELETE);
    }

    public static WithoutBodyBuilder post(@NotBlank String uri) {
        return withoutBody(uri, HttpMethod.PATCH);
    }

    public static WithoutBodyBuilder put(@NotBlank String uri) {
        return withoutBody(uri, HttpMethod.PUT);
    }

    private static WithoutBodyBuilder withoutBody(String uri, HttpMethod httpMethod) {
        return new Builder().uri(uri).method(httpMethod).bodyExists(false);
    }

    public static WithBodyBuilder postWithBody(@NotBlank String uri, Object body) throws JsonProcessingException {
        return withBody(uri, HttpMethod.POST, body);
    }

    public static WithBodyBuilder putWithBody(@NotBlank String uri, Object body) throws JsonProcessingException {
        return withBody(uri, HttpMethod.PUT, body);
    }

    private static WithBodyBuilder withBody(String uri, HttpMethod httpMethod, Object body) throws JsonProcessingException {
        return new Builder().uri(uri).method(httpMethod).bodyExists(true).body(body);
    }

    public interface BaseBuilder {
        RequestConfig build() throws Exception;
    }


    public interface WithoutBodyBuilder extends BaseBuilder {

        WithoutBodyBuilder variables(List<Object> variables);

        WithoutBodyBuilder expectResponse(boolean expectResponse);

        WithoutBodyBuilder httpStatus(HttpStatus httpStatus);
    }

    public interface WithBodyBuilder extends BaseBuilder {
        WithBodyBuilder variables(List<Object> variables);

        WithBodyBuilder expectResponse(boolean expectResponse);

        WithBodyBuilder httpStatus(HttpStatus httpStatus);
    }


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class Builder implements WithBodyBuilder, WithoutBodyBuilder {
        private String uri;
        private HttpMethod method;
        private List<Object> variables = Collections.emptyList();
        private boolean bodyExists;
        private Object body = null;
        private boolean expectResponse = true;
        private HttpStatus httpStatus = HttpStatus.OK;


        @Override
        public RequestConfig build() {
            return new RequestConfig()
                    .setMethod(method)
                    .setUri(uri)
                    .setVariables(variables)
                    .setBodyExists(bodyExists)
                    .setBody(body)
                    .setExpectResponse(expectResponse)
                    .setHttpStatus(httpStatus);
        }
    }
}




