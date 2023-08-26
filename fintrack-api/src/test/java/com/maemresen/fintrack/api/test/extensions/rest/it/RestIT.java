package com.maemresen.fintrack.api.test.extensions.rest.it;

import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.test.config.IntegrationTestConfig;
import com.maemresen.fintrack.api.test.util.context.ContextInitializer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = FintrackApiApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = IntegrationTestConfig.class)
@ActiveProfiles({"it"})
@ExtendWith(RestITExtension.class)
public @interface RestIT {
    @AliasFor(annotation = ContextConfiguration.class, attribute = "initializers")
    Class<? extends ContextInitializer>[] contextInitializer() default {};

    @AliasFor(annotation = ActiveProfiles.class, attribute = "value")
    String[] activeProfiles() default {"it"};
}
