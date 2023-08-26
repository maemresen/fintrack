package com.maemresen.fintrack.api.test.extensions;

import com.maemresen.fintrack.api.FintrackApiApplication;
import com.maemresen.fintrack.api.test.base.AbstractBaseDataLoader;
import com.maemresen.fintrack.api.test.config.RestIntegrationTestConfig;
import com.maemresen.fintrack.api.test.util.container.ContainerManager;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.annotation.DirtiesContext;
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
@ContextConfiguration(classes = RestIntegrationTestConfig.class)
@ActiveProfiles({"it"})
@ExtendWith(RestITExtension.class)
@DirtiesContext
public @interface RestIT {
    String dataSourcePath();

    Class<? extends AbstractBaseDataLoader<?, ?>> dataLoader();

    @AliasFor(annotation = ContextConfiguration.class, attribute = "initializers")
    Class<? extends ContainerManager>[] contextInitializers() default {};
}
