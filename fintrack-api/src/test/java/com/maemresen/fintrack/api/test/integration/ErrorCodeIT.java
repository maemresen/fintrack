package com.maemresen.fintrack.api.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.dto.ErrorCodeDto;
import com.maemresen.fintrack.api.test.base.AbstractBaseRestIT;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.test.util.constant.UseInMemoryDB;
import com.maemresen.fintrack.api.test.util.helper.ApiUriHelper;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@UseInMemoryDB
class ErrorCodeIT extends AbstractBaseRestIT {

    private static final String FIND_ALL = ApiUriHelper.mergeUri(UriConstant.ErrorCode.BASE_URI, UriConstant.ErrorCode.FIND_ALL_URI);

    @Test
    void findAllErrorCodes() throws Exception {
        final var expectedErrorCodes = Arrays.stream(ExceptionType.values())
            .map(ExceptionType::getCode)
            .toList();
        final var expectedErrorNames = Arrays.stream(ExceptionType.values())
            .map(ExceptionType::name)
            .toList();
        var requestConfig = RequestConfig.success(FIND_ALL)
            .requestMethod(HttpMethod.GET)
            .build();
        var errorCodeDtos = performAndReturn(requestConfig, new TypeReference<List<ErrorCodeDto>>() {
        });

        assertTrue(CollectionUtils.isNotEmpty(errorCodeDtos), "Error codes should not be empty");

        var errorCodes = errorCodeDtos.stream()
            .map(ErrorCodeDto::getCode)
            .toList();
        assertTrue(errorCodes.containsAll(expectedErrorCodes), "Error codes should contain all expected error codes");

        var errorNames = errorCodeDtos.stream()
            .map(ErrorCodeDto::getName)
            .toList();
        assertTrue(errorNames.containsAll(expectedErrorNames), "Error names should contain all expected error codes");
    }
}
