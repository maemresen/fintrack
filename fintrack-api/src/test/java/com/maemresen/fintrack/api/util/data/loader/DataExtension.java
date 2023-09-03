package com.maemresen.fintrack.api.util.data.loader;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class DataExtension extends AbstractBaseExtension implements BeforeAllCallback, ParameterResolver {

    private static final String DATA_KEY_PREFIX = "data_";

    private String getDataKey(String name) {
        return DATA_KEY_PREFIX + name;
    }

    private <V> V getData(ExtensionContext extensionContext, String name, Class<V> valueClass) {
        return get(extensionContext, getDataKey(name), valueClass);
    }

    private void storeData(ExtensionContext extensionContext, String name, Object data) {
        store(extensionContext, getDataKey(name), data);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {

        log.info("Initializing IT Test: {}", extensionContext.getDisplayName());

        log.info("Initializing Spring context...");
        var applicationContext = SpringExtension.getApplicationContext(extensionContext);

        log.info("Looking for {} ...", Data.class.getSimpleName());
        var requiredTestClass = extensionContext.getRequiredTestClass();
        var dataAnnotation = AnnotatedElementUtils.findMergedAnnotation(requiredTestClass, Data.class);

        if (dataAnnotation == null) {
            throw new IllegalStateException("Missing " + Data.class.getSimpleName() + " annotation.");
        }

        var sources = dataAnnotation.sources();

        var isAllNamesUnique = Arrays.stream(sources).map(Data.Source::name).distinct().count() == sources.length;
        if (!isAllNamesUnique) {
            throw new IllegalStateException("Data source names must be unique.");
        }

        for (Data.Source source : sources) {
            loadSource(extensionContext, applicationContext, source);
        }
    }

    private void loadSource(ExtensionContext extensionContext, ApplicationContext applicationContext, Data.Source source) throws Exception {
        var path = source.path();
        if (path == null || path.isBlank()) {
            log.warn("Data source not found, skipping data loading.");
            return;
        }

        log.info("Loading data from {} ...", path);
        try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(path)) {
            var dataJson = new String(Objects.requireNonNull(resourceStream).readAllBytes(), StandardCharsets.UTF_8);
            var dataLoader = applicationContext.getBean(source.loader());
            Object data = dataLoader.load(dataJson, applicationContext);
            storeData(extensionContext, source.name(), data);
        }
    }


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var parameter = parameterContext.getParameter();

        if(parameter == null || !parameter.isAnnotationPresent(Data.Name.class)){
            return false;
        }

        var parameterName = parameter.getDeclaredAnnotation(Data.Name.class).value();
        var data = getData(extensionContext, parameterName, parameter.getType());

        var parameterType = parameter.getType();
        return parameterType.isAssignableFrom(data.getClass());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (supportsParameter(parameterContext, extensionContext)) {
            var parameter = parameterContext.getParameter();
            var parameterName = parameter.getDeclaredAnnotation(Data.Name.class).value();
            return getData(extensionContext, parameterName, parameter.getType());
        }

        throw new ParameterResolutionException("Failed to resolve parameter.");
    }
}
