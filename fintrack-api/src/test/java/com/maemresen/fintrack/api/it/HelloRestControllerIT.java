package com.maemresen.fintrack.api.it;

import com.maemresen.fintrack.api.it.util.ApiRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelloRestControllerIT extends AbstractBaseRestIT {

    @Test
    void helloIntegrationTest() throws Exception {
        final ApiRequest build = ApiRequest.success("/api/hello")
            .requestMethod(HttpMethod.GET)
            .build();
        assertDoesNotThrow(() -> requestPerformer.perform(build));
    }
}
