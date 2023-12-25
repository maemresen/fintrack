package com.maemresen.fintrack.commons.spring.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Map;

@Builder
@Getter
public class ApiResponse {
    private Integer httpStatusCode;

    @Singular
    private Map<String, Object> headers;
}
