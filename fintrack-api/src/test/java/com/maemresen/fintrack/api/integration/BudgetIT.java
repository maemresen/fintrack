package com.maemresen.fintrack.api.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.base.AbstractBaseRestIT;
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
import com.maemresen.fintrack.api.util.data.loader.DataSource;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.util.constant.BudgetItConstants;
import com.maemresen.fintrack.api.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.util.helper.ContainerFactory;
import com.maemresen.fintrack.api.util.performer.BudgetRestItService;
import com.maemresen.fintrack.api.util.performer.Performer;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DisplayName("Budget Use Cases IT")
@DataSource(path = "data/budgets.json", loader = BudgetListDataLoader.class)
class BudgetIT extends AbstractBaseRestIT {

    @Autowired
    private Performer performer;

    @Autowired
    private BudgetRestItService budgetRestItService;

    @Container
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = ContainerFactory.createPostgreSQLContainer();

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }


    @Test
    @DisplayName("UC: Budget Retrieval - Find By Id")
    void findById(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.EditBudget.BUDGET_INDEX);
        BudgetDto budgetDto = budgetRestItService.getBudgetById(budgetId);
        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
    }

    @Test
    @DisplayName("UC: Budget Retrieval - Find By Id Not Found")
    void findByIdNotFound() throws Exception {
        final var nonExistingBudgetId = budgetRestItService.getNonExistingBudgetId();
        RequestConfig requestConfig = RequestConfig.error(BudgetItConstants.URI_FIND_BY_ID, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();

        ErrorDto<Long> errorDto = performer.get(requestConfig, new TypeReference<>() {
        });
        assertNotNull(errorDto);
        assertEquals(nonExistingBudgetId, errorDto.getData());
    }

    @Test
    @DisplayName("UC: Budget Retrieval - Find All")
    void findAll(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.EditBudget.BUDGET_INDEX);
        var budgetDtos = budgetRestItService.getBudgets();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(budgetId::equals));
    }

    @Test
    @DisplayName("UC: Create Budget - Create Success")
    void createSuccess() throws Exception {
        String budgetName = budgetRestItService.randomBudgetName();
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_CREATE)
            .requestMethod(HttpMethod.POST)
            .requestBody(new BudgetCreateRequestDto(budgetName))
            .build();
        var createdBudgetDto = performer.get(requestConfig, new TypeReference<BudgetDto>() {
        });
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        Assertions.assertEquals(budgetName, createdBudgetDto.getName());

        var budgetDtos = budgetRestItService.getBudgets();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createdBudgetDto.getId()::equals));
    }

    @Test
    @DisplayName("UC: Create Budget - Create Empty Name Budget")
    void createEmptyNameBudget() throws Exception {
        var invalidCreateRequestDto = new BudgetCreateRequestDto("");
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_CREATE, ExceptionType.INVALID_PARAMETER)
            .requestMethod(HttpMethod.POST)
            .requestBody(invalidCreateRequestDto)
            .expectResponseBody(true)
            .build();
        ErrorDto<List<FieldErrorDto>> responseBody = performer.get(requestConfig, new TypeReference<>() {
        });

        budgetRestItService.assertFieldErrorPresent(responseBody.getData(), BudgetCreateRequestDto.Fields.name, null, null);
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Budget")
    void addStatementToBudgetWithoutInitialStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.AddStatement.NO_INITIAL_STATEMENTS_BUDGET_INDEX);
        budgetRestItService.addStatementToBudget(budgetId, BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT);
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Budget With Initial Statements")
    void addStatementToBudgetWithInitialStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        var budgetDto = budgetRestItService.addStatementToBudget(budgetId, BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT);

        var statementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());

        final var statementId1 = budgetRestItService.getStatementIdByIndex(initialData,
            BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX,
            BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final var statementId2 = budgetRestItService.getStatementIdByIndex(initialData,
            BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX,
            BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        assertTrue(CollectionUtils.containsAll(statementIds, Set.of(statementId1, statementId2)));
    }

    @Test
    @DisplayName("UC: Statement Addition - Add Statement To Non Existing Budget")
    void addStatementToNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = budgetRestItService.getNonExistingBudgetId();
        StatementCreateDto body = StatementCreateDto.builder()
            .amount(BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT)
            .currency(Currency.EUR)
            .type(StatementType.EXPENSE)
            .date(LocalDateTime.now())
            .build();

        var requestConfig = RequestConfig.error(BudgetItConstants.URI_ADD_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.POST)
            .requestBody(body)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();
        ErrorDto<List<FieldErrorDto>> fieldErrorDtoErrorDto = performer.get(requestConfig, new TypeReference<>() {
        });

        budgetRestItService.assertFieldErrorPresent(fieldErrorDtoErrorDto.getData(),
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement With Single Init Statement")
    void removeStatementWithSingleInitStatement(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.RemoveStatement.SINGLE_INITIAL_STATEMENT_BUDGET_INDEX);
        final var statementId1 = budgetRestItService.getStatementIdByIndex(initialData, BudgetItConstants.RemoveStatement.SINGLE_INITIAL_STATEMENT_BUDGET_INDEX, BudgetItConstants.RemoveStatement.SINGLE_INITIAL_STATEMENT_STATEMENT_INDEX);
        var budgetDto = budgetRestItService.removeStatementFromBudget(budgetId, statementId1);
        assertTrue(CollectionUtils.isEmpty(budgetDto.getStatements()));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement With Multiple Init Statements")
    void removeStatementWithMultipleInitStatements(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final var statementId1 = budgetRestItService.getStatementIdByIndex(initialData, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final var statementId2 = budgetRestItService.getStatementIdByIndex(initialData, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        var budgetDto = budgetRestItService.removeStatementFromBudget(budgetId, statementId1);
        var remainingStatementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());
        assertTrue(CollectionUtils.containsAll(remainingStatementIds, Set.of(statementId2)));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Non Exists Statement")
    void removeNonExistsStatement(List<BudgetEntity> initialData) throws Exception {
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final var nonExistingStatementId = budgetRestItService.getNonExistingStatementId();
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performer.get(requestConfig, new TypeReference<>() {
        });

        budgetRestItService.assertFieldErrorPresent(responseBody.getData(),
            BaseEntity.Fields.id,
            StatementEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingStatementId));
    }

    @Test
    @DisplayName("UC: Statement Removal - Remove Statement From Non Existing Budget")
    void removeNonExistsStatementFromNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = budgetRestItService.getNonExistingBudgetId();
        final var nonExistingStatementId = budgetRestItService.getNonExistingStatementId();
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(nonExistingBudgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performer.get(requestConfig, new TypeReference<>() {
        });
        budgetRestItService.assertFieldErrorPresent(responseBody.getData(), BaseEntity.Fields.id, BudgetEntity.class.getName());
    }

    @Test
    @DisplayName("UC: Report - Get Monthly Report For Years (Single Currency)")
    void getMonthlyReportForYearsWithSingleCurrency(List<BudgetEntity> initialData) throws Exception {
        var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.MonthlyReportForYear.SINGLE_CURRENCY_BUDGET_INDEX);
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_MONTHLY_REPORT_FOR_YEAR)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId, BudgetItConstants.MonthlyReportForYear.YEAR))
            .build();

        var expectedReportData = BudgetItConstants.MonthlyReportForYear.SINGLE_CURRENCY_STATEMENTS;
        var expectedAugustSum = budgetRestItService.calculateExpectedSum(expectedReportData, Month.AUGUST, Currency.EUR);
        var expectedSeptemberSum = budgetRestItService.calculateExpectedSum(expectedReportData, Month.SEPTEMBER, Currency.EUR);

        var yearlyReport = performer.get(requestConfig, new TypeReference<List<BudgetReportDto>>() {
        });

        budgetRestItService.assertSummaryMatchForMonthAndCurrencyByYearlyReport(yearlyReport, Month.AUGUST, Currency.EUR, expectedAugustSum);
        budgetRestItService.assertSummaryMatchForMonthAndCurrencyByYearlyReport(yearlyReport, Month.SEPTEMBER, Currency.EUR, expectedSeptemberSum);
    }


    @Test
    @DisplayName("UC: Report - Get Monthly Report For Non Existing Budget")
    void getMonthlyReportForNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = budgetRestItService.getNonExistingBudgetId();
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_MONTHLY_REPORT_FOR_YEAR, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(nonExistingBudgetId, BudgetItConstants.MonthlyReportForYear.YEAR))
            .build();

        var errorDto = performer.get(requestConfig, new TypeReference<ErrorDto<List<FieldErrorDto>>>() {
        });

        var fieldErrorDtos = errorDto.getData();
        budgetRestItService.assertFieldErrorPresent(fieldErrorDtos,
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }

    @Test
    @DisplayName("UC: Report - Get Monthly Report Must be empty for non existing year")
    void getMonthlyReportForNonExistingYear(List<BudgetEntity> initialData) throws Exception {
        final var nonExistingYear = BudgetItConstants.MonthlyReportForYear.YEAR + 1;
        final var budgetId = budgetRestItService.getBudgetIdByIndex(initialData, BudgetItConstants.MonthlyReportForYear.SINGLE_CURRENCY_BUDGET_INDEX);
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_MONTHLY_REPORT_FOR_YEAR)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId, nonExistingYear))
            .build();
        var yearlyReport = performer.get(requestConfig, new TypeReference<List<BudgetReportDto>>() {
        });
        assertTrue(CollectionUtils.isEmpty(yearlyReport), "Yearly report should be empty");
    }
}
