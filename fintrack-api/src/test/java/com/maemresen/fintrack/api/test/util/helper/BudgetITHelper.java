package com.maemresen.fintrack.api.test.util.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.base.BaseDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.test.util.constant.BudgetItConstants;
import com.maemresen.fintrack.api.test.util.performer.Perform;
import com.maemresen.fintrack.api.test.util.performer.PerformAndReturn;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UtilityClass
public class BudgetITHelper {
    public static String randomBudgetName() {
        return StringHelper.randomString("Budget");
    }

    public static Long getBudgetIdByIndex(List<BudgetEntity> budgets, int index) {
        return budgets.get(index).getId();
    }

    public static Long getStatementIdByIndex(List<BudgetEntity> budgets, int budgetIndex, int statementIndex) {
        return budgets.get(budgetIndex).getStatements().get(statementIndex).getId();
    }

    public static Long getNonExistingBudgetId(PerformAndReturn<List<BudgetDto>> performer) throws Exception {
        return getBudgets(performer).stream()
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    public static Long getNonExistingStatementId(PerformAndReturn<List<BudgetDto>> performer) throws Exception {
        return getBudgets(performer).stream()
            .flatMap(budgetEntity -> budgetEntity.getStatements().stream())
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    public static BudgetDto getBudgetById(Long budgetId, PerformAndReturn<BudgetDto> performer) throws Exception {
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_FIND_BY_ID)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId))
            .build();
        return performer.get(requestConfig, new TypeReference<>() {
        });
    }

    public static List<BudgetDto> getBudgets(PerformAndReturn<List<BudgetDto>> performer) throws Exception {
        var requestConfig = RequestConfig.success(String.format(BudgetItConstants.URI_FIND_ALL))
            .requestMethod(HttpMethod.GET)
            .build();
        return performer.get(requestConfig, new TypeReference<>() {
        });
    }

    public static void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName, Predicate<FieldErrorDto> expectedValueMatcher) {
        assertNotNull(fieldErrors, "Field errors should not be null");

        Predicate<FieldErrorDto> predicate = fieldErrorDto -> fieldErrorDto.getField().equals(expectedField) &&
            (expectedClassName == null || fieldErrorDto.getFieldClass().equals(expectedClassName));

        if (expectedValueMatcher != null) {
            predicate = predicate.and(expectedValueMatcher);
        }

        assertTrue(fieldErrors.stream().anyMatch(predicate), constructErrorMessage(expectedField, expectedClassName));
    }

    public static void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName) {
        assertFieldErrorPresent(fieldErrors, expectedField, expectedClassName, null);
    }

    public static String constructErrorMessage(String expectedField, String expectedClassName) {
        return expectedField + " field error not found" + (expectedClassName == null ? "" : " in " + expectedClassName);
    }

    public static RequestConfig getAddStatementRequestConfig(Long budgetId, Double expenseAmount) throws Exception {
        StatementCreateDto body = StatementCreateDto.builder()
            .amount(expenseAmount)
            .currency(Currency.EUR)
            .type(StatementType.EXPENSE)
            .date(LocalDateTime.now())
            .build();

        return RequestConfig.success(BudgetItConstants.URI_ADD_STATEMENT)
            .requestMethod(HttpMethod.POST)
            .requestBody(body)
            .requestVariables(List.of(budgetId))
            .build();
    }

    public static BudgetDto addStatementToBudget(Long budgetId,
                                                 Double amount,
                                                 PerformAndReturn<BudgetDto> getBudgetByIdPerformer,
                                                 PerformAndReturn<BudgetDto> addStatementPerformer) throws Exception {
        final var initialStatementCount = BudgetITHelper.getBudgetById(budgetId, getBudgetByIdPerformer)
            .getStatements()
            .size();

        BudgetDto budgetDto = addStatementPerformer.get(getAddStatementRequestConfig(budgetId, amount), new TypeReference<>() {
        });

        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
        assertNotNull(budgetDto.getStatements());
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(amount)));
        assertEquals(initialStatementCount + 1, CollectionUtils.size(budgetDto.getStatements()));
        return budgetDto;
    }

    public static BudgetDto removeStatementFromBudget(Long budgetId, Long statementId, Perform performer, PerformAndReturn<BudgetDto> budgetByIdPerformer) throws Exception {
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_REMOVE_STATEMENT)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, statementId))
            .expectResponseBody(false)
            .build();
        performer.perform(requestConfig);
        var budgetDto = BudgetITHelper.getBudgetById(budgetId, budgetByIdPerformer);
        assertNotNull(budgetDto);
        return budgetDto;
    }
}
