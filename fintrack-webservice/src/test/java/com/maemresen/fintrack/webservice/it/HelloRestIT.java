package com.maemresen.fintrack.webservice.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.webservice.util.helper.RequestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloRestIT extends AbstractBaseRestITTest {

    @Test
    void test() throws Exception {
        var requestConfig = RequestConfig.success("/hello")
            .requestMethod(HttpMethod.GET)
            .build();

        final var result = requestPerformer.perform(requestConfig, new TypeReference<Map<String, String>>() {
        });

        assertTrue(result.containsKey("message"));
        assertEquals("Hello World!", result.get("message"));
    }
}
