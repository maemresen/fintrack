package com.maemresen.fintrack.api.test.extensions.rest.it;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestIT(activeProfiles = {"it", "test-h2"})
public @interface RestITWithoutDb {
}
