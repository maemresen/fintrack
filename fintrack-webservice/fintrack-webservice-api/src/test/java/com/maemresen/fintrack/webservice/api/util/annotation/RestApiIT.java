package com.maemresen.fintrack.webservice.api.util.annotation;

import com.maemresen.fintrack.webservice.api.FintrackApplication;
import com.maemresen.fintrack.webservice.api.config.RestApiITConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApplication.class)
@ContextConfiguration(classes = RestApiITConfig.class)
@AutoConfigureMockMvc
public @interface RestApiIT {
}
