package com.maemresen.fintrack.api.util.constant;

import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.config.RestApiIntegrationTestConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@ContextConfiguration(classes = RestApiIntegrationTestConfig.class)
@AutoConfigureMockMvc
public @interface RestApiIT {
}
