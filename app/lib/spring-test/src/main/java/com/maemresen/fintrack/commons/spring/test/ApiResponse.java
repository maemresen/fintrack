package com.maemresen.fintrack.commons.spring.test;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
public class ApiResponse {
    private Integer httpStatusCode;

    @Singular
    private Map<String, Object> headers;
}
