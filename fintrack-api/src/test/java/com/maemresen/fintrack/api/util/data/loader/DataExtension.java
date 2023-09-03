package com.maemresen.fintrack.api.util.data.loader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class DataExtension implements BeforeAllCallback, ParameterResolver {

    private static final String IT_DATA_ANNOTATION_KEY = "ITData";
    private static final String IT_DATA_KEY = "data";

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("Initializing IT Data for: {}", extensionContext.getDisplayName());

        log.info("Initializing Spring context...");
        var applicationContext = SpringExtension.getApplicationContext(extensionContext);
        log.info("Spring context initialized.");

        log.info("Looking for {} ...", DataSource.class.getSimpleName());
        var requiredTestClass = extensionContext.getRequiredTestClass();
        var dataSource = AnnotatedElementUtils.findMergedAnnotation(requiredTestClass, DataSource.class);
        if (dataSource == null) {
            log.info("{} restIT not found.", DataSource.class.getSimpleName());
            throw new IllegalStateException("Missing " + DataSource.class.getSimpleName() + " restIT.");
        }
        log.info("{} restIT found: {}", DataSource.class.getSimpleName(), dataSource);
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(IT_DATA_ANNOTATION_KEY, dataSource);

        log.info("Looking data source...");
        var dataSourcePath = dataSource.path();

        if (dataSourcePath == null || dataSourcePath.isBlank()) {
            log.info("Data source not found, skipping data loading.");
            return;
        }

        log.info("Loading data from: {}", dataSourcePath);
        try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(dataSourcePath)) {
            var dataJson = new String(Objects.requireNonNull(resourceStream).readAllBytes(), StandardCharsets.UTF_8);
            var dataLoader = applicationContext.getBean(dataSource.loader());
            Object data = dataLoader.load(dataJson, applicationContext);
            extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(IT_DATA_KEY, data);
            log.info("Data loaded.");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var data = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(IT_DATA_KEY);
        return parameterContext.getParameter().getType().isAssignableFrom(data.getClass());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var data = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(IT_DATA_KEY);
        if (parameterContext.getParameter().getType().isAssignableFrom(data.getClass())) {
            return data;
        }
        throw new ParameterResolutionException("Failed to resolve parameter.");
    }

}
