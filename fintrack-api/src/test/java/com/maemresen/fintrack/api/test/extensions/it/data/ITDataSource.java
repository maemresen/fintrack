package com.maemresen.fintrack.api.test.extensions.it.data;

import com.maemresen.fintrack.api.test.base.AbstractBaseDataLoader;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ITDataExtension.class)
public @interface ITDataSource {
    String dataSourcePath();

    Class<? extends AbstractBaseDataLoader<?, ?>> dataLoader();
}
