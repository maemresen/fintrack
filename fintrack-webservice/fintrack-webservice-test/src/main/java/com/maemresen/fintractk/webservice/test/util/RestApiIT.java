package com.maemresen.fintractk.webservice.test.util;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ContextConfiguration
@AutoConfigureMockMvc
public @interface RestApiIT {

    @AliasFor(annotation = SpringBootTest.class, attribute = "classes")
    Class<?>[] context() default {};

    @AliasFor(annotation = ContextConfiguration.class, attribute = "classes")
    Class<?>[] config() default {};
}
