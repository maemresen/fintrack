package com.maemresen.fintrack.api.util.data.loader;

import com.maemresen.fintrack.api.util.data.loader.base.AbstractBaseDataLoader;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DataExtension.class)
public @interface DataSource {
    String path();

    Class<? extends AbstractBaseDataLoader<?, ?>> loader();
}
