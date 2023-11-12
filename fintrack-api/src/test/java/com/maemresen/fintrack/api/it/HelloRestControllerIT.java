package com.maemresen.fintrack.api.it;

import com.maemresen.fintrack.api.it.util.RequestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelloRestControllerIT extends AbstractBaseRestIT {

    @Test
    void helloIntegrationTest() throws Exception {
        final RequestConfig build = RequestConfig.success("/api/hello")
            .requestMethod(HttpMethod.GET)
            .build();
        assertDoesNotThrow(() -> requestPerformer.perform(build));
    }
}
