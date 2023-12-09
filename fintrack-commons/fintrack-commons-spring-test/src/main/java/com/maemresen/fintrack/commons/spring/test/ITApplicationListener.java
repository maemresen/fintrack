package com.maemresen.fintrack.commons.spring.test;

import com.maemresen.fintrack.commons.spring.test.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class ITApplicationListener implements ApplicationListener<ApplicationContextEvent> {

    @Override
    public void onApplicationEvent(@NotNull ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent || event instanceof ContextStartedEvent) {
            this.loadDataIntoContext(event.getApplicationContext());
        }
    }

    private void loadDataIntoContext(final ApplicationContext applicationContext) {
        final Map<String, DataLoader> dataInitializers = applicationContext.getBeansOfType(DataLoader.class);
        if (dataInitializers.isEmpty()) {
            log.debug("No Initializer found.");
        } else {
            for (Map.Entry<String, DataLoader> entry : dataInitializers.entrySet()) {
                try {
                    log.trace("{} is loading.", entry.getKey());
                    entry.getValue().load();
                    log.debug("{} data loaded.", entry.getKey());
                } catch (Exception e) {
                    log.error("Error while loading {} data", entry.getKey(), e);
                }
            }
            log.info("All {} data loaded successfully.", dataInitializers.size());
        }
    }
}
