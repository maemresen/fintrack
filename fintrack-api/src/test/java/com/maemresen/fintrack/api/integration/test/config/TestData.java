package com.maemresen.fintrack.api.integration.test.config;

import com.maemresen.fintrack.api.integration.test.base.AbstractBaseDataLoader;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TestDataExtension.class)
public @interface TestData {
    Source[] sources() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Source {
        String name();

        String path();

        Class<? extends AbstractBaseDataLoader<?, ?>> loader();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Name {
        String value();
    }
}
