package com.maemresen.fintrack.api.test.extensions.rest.it;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@Slf4j
public class RestITExtension implements BeforeAllCallback, AfterAllCallback {

    private static final String REST_IT_ANNOTATION_KEY = "RestIT";

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("Initializing IT for: {}", extensionContext.getDisplayName());

        log.info("Looking for {} restIT...", RestIT.class.getSimpleName());
        var requiredTestClass = extensionContext.getRequiredTestClass();
        var restIT = AnnotatedElementUtils.findMergedAnnotation(requiredTestClass, RestIT.class);
        if (restIT == null) {
            log.info("{} restIT not found.", RestIT.class.getSimpleName());
            throw new IllegalStateException("Missing " + RestIT.class.getSimpleName() + " restIT.");
        }
        log.info("{} restIT found: {}", RestIT.class.getSimpleName(), restIT);
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(REST_IT_ANNOTATION_KEY, restIT);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.info("Cleaning up containers...");
        var restIT = loadAnnotationFromExtensionStore(context);
        var applicationContext = SpringExtension.getApplicationContext(context);
        Arrays.stream(restIT.contextInitializer()).forEach(containerManagerClass -> {
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
