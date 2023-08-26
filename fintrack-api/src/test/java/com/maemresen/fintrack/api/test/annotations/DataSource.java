package com.maemresen.fintrack.api.test.annotations;

import com.maemresen.fintrack.api.test.base.BaseAbstractDataLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value();
    Class<? extends BaseAbstractDataLoader<?, ?>> loader();
}
