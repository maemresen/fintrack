package com.maemresen.fintrack.api.test.extensions;

import com.maemresen.fintrack.api.test.annotations.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class DataLoaderExtension implements BeforeAllCallback, ParameterResolver {

    private DataSource dataSource;
    private Object data;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        var applicationContext = SpringExtension.getApplicationContext(context);
        var testClass = context.getRequiredTestClass();

        dataSource = testClass.getAnnotation(DataSource.class);
        if (dataSource != null) {
            var dataSourcePath = dataSource.value();
            log.info("Loading data from: {}", dataSourcePath);

            try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(dataSourcePath)) {
                var dataJson = new String(Objects.requireNonNull(resourceStream).readAllBytes(), StandardCharsets.UTF_8);
                var dataLoader = applicationContext.getBean(dataSource.loader());
                data = dataLoader.load(dataJson, applicationContext);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return dataSource != null;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Object ret = null;
        Class<?> parameterClass = parameterContext.getParameter().getType();
        if(parameterClass.isAssignableFrom(data.getClass())) {
            ret = data;
        }
        return ret;
    }
}
