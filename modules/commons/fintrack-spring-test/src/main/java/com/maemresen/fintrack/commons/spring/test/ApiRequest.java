package com.maemresen.fintrack.commons.spring.test;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.springframework.http.HttpMethod;

@Builder
@Getter
public class ApiRequest {

    private HttpMethod requestMethod;
    private String requestUri;

    @Singular
    private List<Object> requestVariables;
    private Object requestBody;
}
