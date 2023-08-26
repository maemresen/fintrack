package com.maemresen.fintrack.api.test.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;

public abstract class BaseAbstractDataLoader<T, R> {

    private final TypeReference<T> typeReference;

    public BaseAbstractDataLoader() {
        this.typeReference = getTypeReference();
    }

    public R load(String dataJson, ApplicationContext applicationContext) throws Exception {
        final var objectMapper = applicationContext.getBean(ObjectMapper.class);
        final var data = objectMapper.readValue(dataJson, this.typeReference);
        return loadData(applicationContext, data);
    }

    protected abstract TypeReference<T> getTypeReference();

    protected abstract R loadData(ApplicationContext applicationContext, T data) throws Exception;
}
