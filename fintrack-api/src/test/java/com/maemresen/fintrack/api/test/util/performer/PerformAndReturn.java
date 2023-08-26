package com.maemresen.fintrack.api.test.util.performer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.test.util.RequestConfig;

public interface PerformAndReturn<T> {
    T get(RequestConfig requestConfig, TypeReference<T> type) throws Exception;
}
