package com.maemresen.fintrack.api.it;

import com.maemresen.fintrack.commons.spring.test.ApiRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelloRestControllerIT extends AbstractBaseRestIT {

    @Test
    void helloIntegrationTest() {
        final var apiRequest = ApiRequest.builder()
            .requestUri("/api/hello")
            .requestMethod(HttpMethod.GET)
            .build();
        assertDoesNotThrow(() -> requestPerformer.performRequest(apiRequest));
    }
}
