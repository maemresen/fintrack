package com.maemresen.fintrack.api.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.ErrorDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.base.BaseDto;
import com.maemresen.fintrack.api.dto.report.BudgetReportDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.test.base.AbstractBaseRestIT;
import com.maemresen.fintrack.api.test.extensions.it.data.ITData;
import com.maemresen.fintrack.api.test.extensions.rest.it.RestIT;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.test.util.context.PostgreSQLContainerManager;
import com.maemresen.fintrack.api.test.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.test.util.helper.BudgetITHelper;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.AddStatement;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.EditBudget;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.MonthlyReportForYear;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.RemoveStatement;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.URI_ADD_STATEMENT;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.URI_CREATE;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.URI_FIND_BY_ID;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.URI_MONTHLY_REPORT_FOR_YEAR;
import static com.maemresen.fintrack.api.test.util.constant.BudgetItConstants.URI_REMOVE_STATEMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Budget Use Cases IT")
@RestIT(contextInitializer = PostgreSQLContainerManager.class)
@ITData(dataSourcePath = "data/budgets.json", dataLoader = BudgetListDataLoader.class)
class BudgetIT extends AbstractBaseRestIT {

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
    @DisplayName("UC: Budget Retrieval - Find By Id")
    void findById(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, EditBudget.BUDGET_INDEX);
        BudgetDto budgetDto = BudgetITHelper.getBudgetById(budgetId, BudgetIT.this::performAndReturn);
        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
    }

    @Test
    @DisplayName("UC: Budget Retrieval - Find By Id Not Found")
    void findByIdNotFound() throws Exception {
        final var nonExistingBudgetId = BudgetITHelper.getNonExistingBudgetId(BudgetIT.this::performAndReturn);
        RequestConfig requestConfig = RequestConfig.error(URI_FIND_BY_ID, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();

        ErrorDto<Long> errorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });
        assertNotNull(errorDto);
        assertEquals(nonExistingBudgetId, errorDto.getData());
    }

    @Test
    @DisplayName("UC: Budget Retrieval - Find All")
    void findAll(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, EditBudget.BUDGET_INDEX);
        var budgetDtos = BudgetITHelper.getBudgets(BudgetIT.this::performAndReturn);
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(budgetId::equals));
    }

    @Test
    @DisplayName("UC: Create Budget - Create Success")
    void createSuccess() throws Exception {
        String budgetName = BudgetITHelper.randomBudgetName();
        var requestConfig = RequestConfig.success(URI_CREATE)
            .requestMethod(HttpMethod.POST)
            .requestBody(new BudgetCreateRequestDto(budgetName))
            .build();
        var createdBudgetDto = performAndReturn(requestConfig, new TypeReference<BudgetDto>() {
        });
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        assertEquals(budgetName, createdBudgetDto.getName());

        var budgetDtos = BudgetITHelper.getBudgets(BudgetIT.this::performAndReturn);
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createdBudgetDto.getId()::equals));
    }

    @Test
    @DisplayName("UC: Create Budget - Create Empty Name Budget")
    void createEmptyNameBudget() throws Exception {
        var invalidCreateRequestDto = new BudgetCreateRequestDto("");
        var requestConfig = RequestConfig.error(URI_CREATE, ExceptionType.INVALID_PARAMETER)
            .requestMethod(HttpMethod.POST)
            .requestBody(invalidCreateRequestDto)
            .expectResponseBody(true)
            .build();
        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetITHelper.assertFieldErrorPresent(responseBody.getData(), BudgetCreateRequestDto.Fields.name, null, null);
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Budget")
    void addStatementToBudgetWithoutInitialStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, AddStatement.NO_INITIAL_STATEMENTS_BUDGET_INDEX);
        BudgetITHelper.addStatementToBudget(budgetId, STATEMENT_FOR_ADD_STATEMENT_AMOUNT, BudgetIT.this::performAndReturn, BudgetIT.this::performAndReturn);
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Budget With Initial Statements")
    void addStatementToBudgetWithInitialStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        var budgetDto = BudgetITHelper.addStatementToBudget(budgetId, STATEMENT_FOR_ADD_STATEMENT_AMOUNT, BudgetIT.this::performAndReturn, BudgetIT.this::performAndReturn);

        var statementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());

        final var statementId1 = BudgetITHelper.getStatementIdByIndex(initialData,
            AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX,
            AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final var statementId2 = BudgetITHelper.getStatementIdByIndex(initialData,
            AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX,
            AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        assertTrue(CollectionUtils.containsAll(statementIds, Set.of(statementId1, statementId2)));
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Non Existing Budget")
    void addStatementToNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = BudgetITHelper.getNonExistingBudgetId(BudgetIT.this::performAndReturn);
        StatementCreateDto body = StatementCreateDto.builder()
            .amount(STATEMENT_FOR_ADD_STATEMENT_AMOUNT)
            .currency(Currency.EUR)
            .type(StatementType.EXPENSE)
            .date(LocalDateTime.now())
            .build();

        var requestConfig = RequestConfig.error(URI_ADD_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.POST)
            .requestBody(body)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();
        ErrorDto<List<FieldErrorDto>> fieldErrorDtoErrorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetITHelper.assertFieldErrorPresent(fieldErrorDtoErrorDto.getData(),
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement With Single Init Statement")
    void removeStatementWithSingleInitStatement(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, RemoveStatement.SINGLE_INITIAL_STATEMENT_BUDGET_INDEX);
        final var statementId1 = BudgetITHelper.getStatementIdByIndex(initialData, RemoveStatement.SINGLE_INITIAL_STATEMENT_BUDGET_INDEX, RemoveStatement.SINGLE_INITIAL_STATEMENT_STATEMENT_INDEX);
        var budgetDto = BudgetITHelper.removeStatementFromBudget(budgetId, statementId1, BudgetIT.this::perform, BudgetIT.this::performAndReturn);
        assertTrue(CollectionUtils.isEmpty(budgetDto.getStatements()));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement With Multiple Init Statements")
    void removeStatementWithMultipleInitStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final var statementId1 = BudgetITHelper.getStatementIdByIndex(initialData, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final var statementId2 = BudgetITHelper.getStatementIdByIndex(initialData, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        var budgetDto = BudgetITHelper.removeStatementFromBudget(budgetId, statementId1, BudgetIT.this::perform, BudgetIT.this::performAndReturn);
        var remainingStatementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());
        assertTrue(CollectionUtils.containsAll(remainingStatementIds, Set.of(statementId2)));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Non Exists Statement")
    void removeNonExistsStatement(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final var nonExistingStatementId = BudgetITHelper.getNonExistingStatementId(BudgetIT.this::performAndReturn);
        var requestConfig = RequestConfig.error(URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetITHelper.assertFieldErrorPresent(responseBody.getData(),
            BaseEntity.Fields.id,
            StatementEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingStatementId));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement From Non Existing Budget")
    void removeNonExistsStatementFromNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = BudgetITHelper.getNonExistingBudgetId(BudgetIT.this::performAndReturn);
        final var nonExistingStatementId = BudgetITHelper.getNonExistingStatementId(BudgetIT.this::performAndReturn);
        var requestConfig = RequestConfig.error(URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(nonExistingBudgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });
        BudgetITHelper.assertFieldErrorPresent(responseBody.getData(), BaseEntity.Fields.id, BudgetEntity.class.getName());
    }

    @Test
    @DisplayName("UC: Report - Get Monthly Report For Years (Single Currency)")
    void getMonthlyReportForYearsWithSingleCurrency(List<BudgetEntity> initialData) throws Exception {
        var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, MonthlyReportForYear.SINGLE_CURRENCY_BUDGET_INDEX);
        var requestConfig = RequestConfig.success(URI_MONTHLY_REPORT_FOR_YEAR)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId, MonthlyReportForYear.YEAR))
            .build();

        var expectedReportData = MonthlyReportForYear.SINGLE_CURRENCY_STATEMENTS;
        var expectedAugustSum = BudgetITHelper.calculateExpectedSum(expectedReportData, Month.AUGUST, Currency.EUR);
        var expectedSeptemberSum = BudgetITHelper.calculateExpectedSum(expectedReportData, Month.SEPTEMBER, Currency.EUR);

        var yearlyReport = performAndReturn(requestConfig, new TypeReference<List<BudgetReportDto>>() {
        });

        BudgetITHelper.assertSummaryMatchForMonthAndCurrencyByYearlyReport(yearlyReport, Month.AUGUST, Currency.EUR, expectedAugustSum);
        BudgetITHelper.assertSummaryMatchForMonthAndCurrencyByYearlyReport(yearlyReport, Month.SEPTEMBER, Currency.EUR, expectedSeptemberSum);
    }


    @Test
    @DisplayName("UC: Report - Get Monthly Report For Non Existing Budget")
    void getMonthlyReportForNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = BudgetITHelper.getNonExistingBudgetId(BudgetIT.this::performAndReturn);
        var requestConfig = RequestConfig.error(URI_MONTHLY_REPORT_FOR_YEAR, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(nonExistingBudgetId, MonthlyReportForYear.YEAR))
            .build();

        var errorDto = performAndReturn(requestConfig, new TypeReference<ErrorDto<List<FieldErrorDto>>>() {
        });

        var fieldErrorDtos = errorDto.getData();
        BudgetITHelper.assertFieldErrorPresent(fieldErrorDtos,
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }

    @Test
    @DisplayName("UC: Report - Get Monthly Report Must be empty for non existing year")
    void getMonthlyReportForNonExistingYear(List<BudgetEntity> initialData) throws Exception {
        final var nonExistingYear = MonthlyReportForYear.YEAR + 1;
        final var budgetId = BudgetITHelper.getBudgetIdByIndex(initialData, MonthlyReportForYear.SINGLE_CURRENCY_BUDGET_INDEX);
        var requestConfig = RequestConfig.success(URI_MONTHLY_REPORT_FOR_YEAR)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId, nonExistingYear))
            .build();
        var yearlyReport = performAndReturn(requestConfig, new TypeReference<List<BudgetReportDto>>() {
        });
        assertTrue(CollectionUtils.isEmpty(yearlyReport), "Yearly report should be empty");
    }
}
