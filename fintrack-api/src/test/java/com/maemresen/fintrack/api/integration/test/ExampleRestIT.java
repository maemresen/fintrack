package com.maemresen.fintrack.api.integration.test;

import com.maemresen.fintrack.api.integration.test.base.AbstractBaseRestIT;
import com.maemresen.fintrack.api.integration.test.util.RequestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

class ExampleRestIT extends AbstractBaseRestIT {

    @Test
    void helloTest() throws Exception {
        final RequestConfig build = RequestConfig.success("/api/hello")
            .requestMethod(HttpMethod.GET)
            .build();
        requestPerformer.perform(build);
    }

}
