package com.maemresen.fintrack.api.test.util.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public abstract class ContainerManager implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    protected void addSingleProperty(ConfigurableApplicationContext applicationContext, String key, String value) {
        ConfigurableEnvironment configurableEnvironment = applicationContext.getEnvironment();
        configurableEnvironment.getPropertySources().addFirst(new MapPropertySource(key, Map.of(key, value)));
    }

    public abstract void cleanup();
}
