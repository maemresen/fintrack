package com.maemresen.fintrack.api.integration.test.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractBaseDataLoader<T, R> {

    private final TypeReference<T> typeReference;

    public R load(String dataJson, ApplicationContext applicationContext) throws Exception {
        final var objectMapper = applicationContext.getBean(ObjectMapper.class);

        log.info("Parsing data from: {}", dataJson);
        final var data = objectMapper.readValue(dataJson, this.typeReference);
        log.info("Data parsed.");
        return loadData(applicationContext, data);
    }

    protected abstract R loadData(ApplicationContext applicationContext, T data) throws Exception;
}
