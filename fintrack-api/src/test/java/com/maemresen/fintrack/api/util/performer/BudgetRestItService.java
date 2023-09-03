package com.maemresen.fintrack.api.util.performer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.base.BaseDto;
import com.maemresen.fintrack.api.dto.report.BudgetReportDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.util.constant.BudgetItConstants;
import com.maemresen.fintrack.api.util.helper.StringHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class BudgetRestItService {
    private final Performer performer;

    public String randomBudgetName() {
        return StringHelper.randomString("Budget");
    }

    public Long getBudgetIdByIndex(List<BudgetEntity> budgets, int index) {
        return budgets.get(index).getId();
    }

    public Long getStatementIdByIndex(List<BudgetEntity> budgets, int budgetIndex, int statementIndex) {
        return budgets.get(budgetIndex).getStatements().get(statementIndex).getId();
    }

    public Long getNonExistingBudgetId() throws Exception {
        return getBudgets().stream()
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    public Long getNonExistingStatementId() throws Exception {
        return getBudgets().stream()
            .flatMap(budgetEntity -> budgetEntity.getStatements().stream())
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    public BudgetDto getBudgetById(Long budgetId) throws Exception {
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_FIND_BY_ID)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId))
            .build();
        return performer.get(requestConfig, new TypeReference<>() {
        });
    }

    public List<BudgetDto> getBudgets() throws Exception {
        var requestConfig = RequestConfig.success(String.format(BudgetItConstants.URI_FIND_ALL))
            .requestMethod(HttpMethod.GET)
            .build();
        return performer.get(requestConfig, new TypeReference<>() {
        });
    }

    public void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName, Predicate<FieldErrorDto> expectedValueMatcher) {
        assertNotNull(fieldErrors, "Field errors should not be null");

        Predicate<FieldErrorDto> predicate = fieldErrorDto -> fieldErrorDto.getField().equals(expectedField) &&
            (expectedClassName == null || fieldErrorDto.getFieldClass().equals(expectedClassName));

        if (expectedValueMatcher != null) {
            predicate = predicate.and(expectedValueMatcher);
        }

        assertTrue(fieldErrors.stream().anyMatch(predicate), constructErrorMessage(expectedField, expectedClassName));
    }

    public void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName) {
        assertFieldErrorPresent(fieldErrors, expectedField, expectedClassName, null);
    }

    public String constructErrorMessage(String expectedField, String expectedClassName) {
        return expectedField + " field error not found" + (expectedClassName == null ? "" : " in " + expectedClassName);
    }

    public RequestConfig getAddStatementRequestConfig(Long budgetId, Double expenseAmount) throws Exception {
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

    public BudgetDto addStatementToBudget(Long budgetId, Double amount) throws Exception {
        final var initialStatementCount = getBudgetById(budgetId)
            .getStatements()
            .size();

        BudgetDto budgetDto = performer.get(getAddStatementRequestConfig(budgetId, amount), new TypeReference<>() {
        });

        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
        assertNotNull(budgetDto.getStatements());
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(amount)));
        assertEquals(initialStatementCount + 1, CollectionUtils.size(budgetDto.getStatements()));
        return budgetDto;
    }

    public BudgetDto removeStatementFromBudget(Long budgetId, Long statementId) throws Exception {
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_REMOVE_STATEMENT)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, statementId))
            .expectResponseBody(false)
            .build();
        performer.perform(requestConfig);
        var budgetDto = getBudgetById(budgetId);
        assertNotNull(budgetDto);
        return budgetDto;
    }

    public double calculateExpectedSum(List<BudgetItConstants.MonthlyReportForYear.Statement> statements, Month month, Currency currency) {
        return statements.stream()
            .filter(statement -> statement.date().getMonth().equals(month) && statement.currency().equals(currency))
            .mapToDouble(statement -> statement.amount() * (statement.type() == StatementType.INCOME ? 1 : -1))
            .sum();
    }

    public void assertSummaryMatchForMonthAndCurrencyByYearlyReport(List<BudgetReportDto> yearlyReport, Month month, Currency currency, double expectedSummary) {
        var optionalMonthSum = yearlyReport.stream()
            .filter(report -> report.getMonth().equals(month))
            .findFirst();
        assertTrue(optionalMonthSum.isPresent(), month + " sum should be present");

        var monthReportDto = optionalMonthSum.get();
        var monthReportCurrencySums = monthReportDto.getSums();

        assertEquals(1, monthReportCurrencySums.size());

        var monthCurrencySumOptional = monthReportCurrencySums.stream()
            .filter(summary -> summary.getCurrency().equals(currency))
            .findFirst();

        assertTrue(monthCurrencySumOptional.isPresent(), month + " " + currency + " sum should be present");
        var monthCurrencySum = monthCurrencySumOptional.get();

        assertEquals(expectedSummary, monthCurrencySum.getSum());
    }
}
