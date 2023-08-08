package com.maemresen.fintrack.api.util;

import com.maemresen.fintrack.api.utils.constants.ExceptionType;
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

@Accessors(chain = true)
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public class RequestConfig {

    private HttpMethod requestMethod;
    private String requestUri;
    private List<Object> requestVariables;
    private Object requestBody;
    private boolean expectResponseBody;
    private HttpStatus responseHttpStatus;
    private ExceptionType responseExceptionType;

    public static DefaultBuilder success(@NotBlank String uri) {
        return new Builder().requestUri(uri).responseExceptionType(null);
    }

    public static ErrorBuilder error(@NotBlank String uri, @NotBlank ExceptionType exceptionType) {
        return new Builder().requestUri(uri)
                .responseExceptionType(exceptionType)
                .responseHttpStatus(exceptionType.getHttpStatus());
    }

    public interface BaseBuilder {
        RequestConfig build() throws Exception;
    }

    public interface DefaultBuilder extends BaseBuilder {
        DefaultBuilder requestMethod(HttpMethod method);

        DefaultBuilder requestVariables(List<Object> variables);

        DefaultBuilder requestBody(Object body);

        DefaultBuilder expectResponseBody(boolean expectResponse);
    }

    public interface ErrorBuilder extends BaseBuilder {
        ErrorBuilder requestMethod(HttpMethod method);

        ErrorBuilder requestVariables(List<Object> variables);

        ErrorBuilder requestBody(Object body);

        DefaultBuilder expectResponseBody(boolean expectResponse);

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(fluent = true, chain = true)
    @Setter
    public static class Builder implements DefaultBuilder, ErrorBuilder {
        private String requestUri;
        private HttpMethod requestMethod;
        private List<Object> requestVariables = Collections.emptyList();
        private Object requestBody = null;
        private boolean expectResponseBody = true;
        private HttpStatus responseHttpStatus = HttpStatus.OK;
        private ExceptionType responseExceptionType = null;

        @Override
        public RequestConfig build() {
            return new RequestConfig()
                    .setRequestUri(requestUri)
                    .setRequestMethod(requestMethod)
                    .setRequestBody(requestBody)
                    .setRequestVariables(requestVariables)
                    .setRequestBody(requestBody)
                    .setExpectResponseBody(expectResponseBody)
                    .setResponseHttpStatus(responseHttpStatus)
                    .setResponseExceptionType(responseExceptionType);
        }
    }
}




