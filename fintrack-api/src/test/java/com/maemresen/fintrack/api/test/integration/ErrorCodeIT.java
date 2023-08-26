package com.maemresen.fintrack.api.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.dto.ErrorCodeDto;
import com.maemresen.fintrack.api.test.base.AbstractBaseRestIT;
import com.maemresen.fintrack.api.test.extensions.RestIT;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.test.util.container.PostgreSQLContainerManager;
import com.maemresen.fintrack.api.test.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.test.util.helper.ApiUriHelper;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@RestIT(dataSourcePath = "data/budgets.json",
    dataLoader = BudgetListDataLoader.class,
    contextInitializers = PostgreSQLContainerManager.class
)
class ErrorCodeIT extends AbstractBaseRestIT {

    private static final String FIND_ALL = ApiUriHelper.mergeUri(UriConstant.ErrorCode.BASE_URI, UriConstant.ErrorCode.FIND_ALL_URI);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

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
