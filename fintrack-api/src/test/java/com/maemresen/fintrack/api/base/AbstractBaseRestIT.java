package com.maemresen.fintrack.api.base;

import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.config.RestApiIntegrationTestConfig;
import com.maemresen.fintrack.api.util.performer.Performer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@ContextConfiguration(classes = RestApiIntegrationTestConfig.class)
@AutoConfigureMockMvc
public abstract class AbstractBaseRestIT {

    @Autowired
    protected Performer performer;
}
