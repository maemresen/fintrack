package com.maemresen.fintrack.commons.spring.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.http.HttpMethod;

import java.util.List;

@Builder
@Getter
public class ApiRequest {

    private HttpMethod requestMethod;
    private String requestUri;

    @Singular
    private List<Object> requestVariables;
    private Object requestBody;
}
