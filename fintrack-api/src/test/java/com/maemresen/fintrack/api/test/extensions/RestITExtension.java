package com.maemresen.fintrack.api.test.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class RestITExtension implements BeforeAllCallback, ParameterResolver, AfterAllCallback {

    private static final String REST_IT_ANNOTATION_KEY = "RestIT";
    private static final String DATA_KEY = "data";

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("Initializing IT for: {}", extensionContext.getDisplayName());

        log.info("Initializing Spring context...");
        var applicationContext = SpringExtension.getApplicationContext(extensionContext);
        log.info("Spring context initialized.");

        log.info("Looking for {} restIT...", RestIT.class.getSimpleName());
        var restIT = extensionContext.getRequiredTestClass().getAnnotation(RestIT.class);
        if (restIT == null) {
            log.info("{} restIT not found.", RestIT.class.getSimpleName());
            throw new IllegalStateException("Missing " + RestIT.class.getSimpleName() + " restIT.");
        }
        log.info("{} restIT found: {}", RestIT.class.getSimpleName(), restIT);
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(REST_IT_ANNOTATION_KEY, restIT);

        log.info("Looking data source...");
        var dataSourcePath = restIT.dataSourcePath();

        if (dataSourcePath == null || dataSourcePath.isBlank()) {
            log.info("Data source not found, skipping data loading.");
            return;
        }

        log.info("Loading data from: {}", dataSourcePath);
        try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(dataSourcePath)) {
            var dataJson = new String(Objects.requireNonNull(resourceStream).readAllBytes(), StandardCharsets.UTF_8);
            var dataLoader = applicationContext.getBean(restIT.dataLoader());
            Object data = dataLoader.load(dataJson, applicationContext);
            extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(DATA_KEY, data);
            log.info("Data loaded.");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var data = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(DATA_KEY);
        return parameterContext.getParameter().getType().isAssignableFrom(data.getClass());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var data = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(DATA_KEY);
        if (parameterContext.getParameter().getType().isAssignableFrom(data.getClass())) {
            return data;
        }
        throw new ParameterResolutionException("Failed to resolve parameter.");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        log.info("Cleaning up containers...");
        var restIT = loadAnnotationFromExtensionStore(context);
        var applicationContext = SpringExtension.getApplicationContext(context);
        Arrays.stream(restIT.contextInitializers()).forEach(containerManagerClass -> {
            log.info("Cleaning up container: {}", containerManagerClass.getSimpleName());
            var containerManager = applicationContext.getBean(containerManagerClass);
            containerManager.cleanup();
        });
    }

    private static RestIT loadAnnotationFromExtensionStore(ExtensionContext extensionContext) {
        var annotation = extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(REST_IT_ANNOTATION_KEY, RestIT.class);

        if (annotation == null) {
            throw new IllegalStateException("Missing " + RestIT.class.getSimpleName() + " annotation.");
        }

        return annotation;
    }
}
