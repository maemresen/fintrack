package com.maemresen.fintrack.commons.spring.test;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContainerHolder<T extends GenericContainer<?>> {
    private final String name;

    @Getter
    private final T container;

    public void restart() {
        stop();
        start();
    }

    public void start() {
        log.trace("{} is starting", name);
        container.start();
        log.debug("{} started with instance {}", name, container.getContainerId());
    }

    public void stop() {
        if (container.isCreated() || container.isRunning()) {
            log.trace("{} has running instance with id {}. Stopping it.", name, container.getContainerId());
            final var containerId = container.getContainerId();
            container.stop();
            log.debug("{} instance stopped.", containerId);
        } else {
            log.debug("{} has not any running instance to stop.", name);
        }
    }
}
