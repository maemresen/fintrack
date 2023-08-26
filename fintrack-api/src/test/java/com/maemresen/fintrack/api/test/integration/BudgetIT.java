package com.maemresen.fintrack.api.test.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.ErrorDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.base.BaseDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.test.annotations.DataSource;
import com.maemresen.fintrack.api.test.base.AbstractBaseRestIT;
import com.maemresen.fintrack.api.test.extensions.DataLoaderExtension;
import com.maemresen.fintrack.api.test.extensions.PostgreSQLExtension;
import com.maemresen.fintrack.api.test.util.RequestConfig;
import com.maemresen.fintrack.api.test.util.constant.BudgetItConstants;
import com.maemresen.fintrack.api.test.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.test.util.helper.BudgetIntegrationTestHelper;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PostgreSQLExtension.class)
@ExtendWith(DataLoaderExtension.class)
@DataSource(value = "data/budgets.json", loader = BudgetListDataLoader.class)
class BudgetIT extends AbstractBaseRestIT {

    private static List<BudgetEntity> INITIAL_BUDGETS;

    @BeforeAll
    static void beforeAll(List<BudgetEntity> budgets) {
        INITIAL_BUDGETS = budgets;
    }

    @Test
    void findById() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_EDIT_TEST);
        BudgetDto budgetDto = BudgetIntegrationTestHelper.getBudgetById(budgetId, this::performAndReturn);
        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
    }

    @Test
    void findByIdNotFound() throws Exception {
        final var nonExistingBudgetId = BudgetIntegrationTestHelper.getNonExistingBudgetId(this::performAndReturn);
        RequestConfig requestConfig = RequestConfig.error(BudgetItConstants.URI_FIND_BY_ID, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();

        ErrorDto<Long> errorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });
        assertNotNull(errorDto);
        assertEquals(nonExistingBudgetId, errorDto.getData());
    }

    @Test
    void findAll() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_EDIT_TEST);
        var budgetDtos = BudgetIntegrationTestHelper.getBudgets(this::performAndReturn);
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(budgetId::equals));
    }


    @Test
    void createSuccess() throws Exception {
        String budgetName = BudgetIntegrationTestHelper.randomBudgetName();
        var requestConfig = RequestConfig.success(BudgetItConstants.URI_CREATE)
            .requestMethod(HttpMethod.POST)
            .requestBody(new BudgetCreateRequestDto(budgetName))
            .build();
        var createdBudgetDto = performAndReturn(requestConfig, new TypeReference<BudgetDto>() {
        });
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        assertEquals(budgetName, createdBudgetDto.getName());

        var budgetDtos = BudgetIntegrationTestHelper.getBudgets(this::performAndReturn);
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createdBudgetDto.getId()::equals));
    }

    @Test
    void createEmptyNameBudget() throws Exception {
        var invalidCreateRequestDto = new BudgetCreateRequestDto("");
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_CREATE, ExceptionType.INVALID_PARAMETER)
            .requestMethod(HttpMethod.POST)
            .requestBody(invalidCreateRequestDto)
            .expectResponseBody(true)
            .build();
        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetIntegrationTestHelper.assertFieldErrorPresent(responseBody.getData(), BudgetCreateRequestDto.Fields.name, null, null);
    }

    @Test
    void addStatementToBudgetWithoutInitialStatements() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_NO_INITIAL_STATEMENTS);
        BudgetIntegrationTestHelper.addStatementToBudget(budgetId, BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT, this::performAndReturn, this::performAndReturn);
    }

    @Test
    void addStatementToBudgetWithInitialStatements() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS);
        var budgetDto = BudgetIntegrationTestHelper.addStatementToBudget(budgetId, BudgetItConstants.STATEMENT_FOR_ADD_STATEMENT_AMOUNT, this::performAndReturn, this::performAndReturn);

        var statementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());

        final var statementId1 = BudgetIntegrationTestHelper.getStatementIdByIndex(INITIAL_BUDGETS,
            BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS,
            BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1);
        final var statementId2 = BudgetIntegrationTestHelper.getStatementIdByIndex(INITIAL_BUDGETS,
            BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS,
            BudgetItConstants.BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2);
        assertTrue(CollectionUtils.containsAll(statementIds, Set.of(statementId1, statementId2)));
    }

    @Test
    void addStatementToNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = BudgetIntegrationTestHelper.getNonExistingBudgetId(this::performAndReturn);
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
        ErrorDto<List<FieldErrorDto>> fieldErrorDtoErrorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetIntegrationTestHelper.assertFieldErrorPresent(fieldErrorDtoErrorDto.getData(),
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }

    @Test
    void removeStatementWithSingleInitStatement() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_SINGLE_STATEMENT_REMOVE);
        final var statementId1 = BudgetIntegrationTestHelper.getStatementIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_SINGLE_STATEMENT_REMOVE, BudgetItConstants.BUDGET_FOR_SINGLE_STATEMENT_REMOVE_STATEMENT_1);
        var budgetDto = BudgetIntegrationTestHelper.removeStatementFromBudget(budgetId, statementId1, this::perform, this::performAndReturn);
        assertTrue(CollectionUtils.isEmpty(budgetDto.getStatements()));
    }

    @Test
    void removeStatementWithMultipleInitStatements() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE);
        final var statementId1 = BudgetIntegrationTestHelper.getStatementIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_STATEMENT_1);
        final var statementId2 = BudgetIntegrationTestHelper.getStatementIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_STATEMENT_2);
        var budgetDto = BudgetIntegrationTestHelper.removeStatementFromBudget(budgetId, statementId1, this::perform, this::performAndReturn);
        var remainingStatementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());
        assertTrue(CollectionUtils.containsAll(remainingStatementIds, Set.of(statementId2)));
    }

    @Test
    void removeNonExistsStatement() throws Exception {
        final var budgetId = BudgetIntegrationTestHelper.getBudgetIdByIndex(INITIAL_BUDGETS, BudgetItConstants.BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE);
        final var nonExistingStatementId = BudgetIntegrationTestHelper.getNonExistingStatementId(this::performAndReturn);
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        BudgetIntegrationTestHelper.assertFieldErrorPresent(responseBody.getData(),
            BaseEntity.Fields.id,
            StatementEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingStatementId));
    }

    @Test
    void removeNonExistsStatementFromNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = BudgetIntegrationTestHelper.getNonExistingBudgetId(this::performAndReturn);
        final var nonExistingStatementId = BudgetIntegrationTestHelper.getNonExistingStatementId(this::performAndReturn);
        var requestConfig = RequestConfig.error(BudgetItConstants.URI_REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(nonExistingBudgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });
        BudgetIntegrationTestHelper.assertFieldErrorPresent(responseBody.getData(), BaseEntity.Fields.id, BudgetEntity.class.getName());
    }
}
