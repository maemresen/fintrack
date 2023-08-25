package com.maemresen.fintrack.api.integration.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.annotations.DataSource;
import com.maemresen.fintrack.api.base.AbstractBaseRestIT;
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
import com.maemresen.fintrack.api.extensions.DataLoaderExtension;
import com.maemresen.fintrack.api.extensions.PostgreSQLExtension;
import com.maemresen.fintrack.api.util.ApiUriUtil;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.util.StringHelper;
import com.maemresen.fintrack.api.util.data.loader.BudgetListDataLoader;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.UriConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PostgreSQLExtension.class)
@ExtendWith(DataLoaderExtension.class)
@DataSource(value = "data/budgets.json", loader = BudgetListDataLoader.class)
class BudgetIT extends AbstractBaseRestIT {

    private static final String CREATE = ApiUriUtil.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.CREATE);
    private static final String FIND_BY_ID = ApiUriUtil.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_BY_ID);
    private static final String FIND_ALL = ApiUriUtil.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.FIND_ALL);
    private static final String ADD_STATEMENT = ApiUriUtil.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.ADD_STATEMENT);
    private static final String REMOVE_STATEMENT = ApiUriUtil.mergeUri(UriConstant.Budget.BASE_URI, UriConstant.Budget.REMOVE_STATEMENT);

    private static final int BUDGET_FOR_EDIT_TEST = 0;
    private static final int BUDGET_FOR_STATEMENT_ADDITION_NO_INITIAL_STATEMENTS = 1;

    private static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS = 2;
    private static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1 = 0;
    private static final int BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2 = 1;

    private static final int BUDGET_FOR_SINGLE_STATEMENT_REMOVE = 3;
    private static final int BUDGET_FOR_SINGLE_STATEMENT_REMOVE_STATEMENT_1 = 0;

    private static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE = 4;
    private static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_SATATEMENT_1 = 0;
    private static final int BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_SATATEMENT_2 = 1;

    private static final Double STATEMENT_FOR_ADD_STATEMENT_AMOUNT = 100D;

    private static List<BudgetEntity> INITIAL_BUDGETS;

    @BeforeAll
    static void beforeAll(List<BudgetEntity> budgets) {
        INITIAL_BUDGETS = budgets;
    }

    private static Long getBudgetIdByIndex(int index) {
        return INITIAL_BUDGETS.get(index).getId();
    }

    private static Long getStatementIdByIndex(int budgetIndex, int statementIndex) {
        return INITIAL_BUDGETS.get(budgetIndex).getStatements().get(statementIndex).getId();
    }

    private String randomBudgetName() {
        return StringHelper.randomString("Budget");
    }

    private Long getNonExistingBudgetId() throws Exception {
        return performSuccessfulFindAllRequest().stream()
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    private Long getNonExistingStatementId() throws Exception {
        return performSuccessfulFindAllRequest().stream()
            .flatMap(budgetEntity -> budgetEntity.getStatements().stream())
            .map(BaseDto::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }

    private BudgetDto performSuccessfulFindByIdRequest(Long budgetId) throws Exception {
        var requestConfig = RequestConfig.success(FIND_BY_ID)
            .requestMethod(HttpMethod.GET)
            .requestVariables(List.of(budgetId))
            .build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    private List<BudgetDto> performSuccessfulFindAllRequest() throws Exception {
        var requestConfig = RequestConfig.success(String.format(FIND_ALL))
            .requestMethod(HttpMethod.GET)
            .build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    private void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName, Predicate<FieldErrorDto> expectedValueMatcher) {
        assertNotNull(fieldErrors, "Field errors should not be null");

        Predicate<FieldErrorDto> predicate = fieldErrorDto -> fieldErrorDto.getField().equals(expectedField) &&
            (expectedClassName == null || fieldErrorDto.getFieldClass().equals(expectedClassName));

        if (expectedValueMatcher != null) {
            predicate = predicate.and(expectedValueMatcher);
        }

        assertTrue(fieldErrors.stream().anyMatch(predicate), constructErrorMessage(expectedField, expectedClassName));
    }

    private void assertFieldErrorPresent(List<FieldErrorDto> fieldErrors, String expectedField, String expectedClassName) {
        assertFieldErrorPresent(fieldErrors, expectedField, expectedClassName, null);
    }

    private String constructErrorMessage(String expectedField, String expectedClassName) {
        return expectedField + " field error not found" + (expectedClassName == null ? "" : " in " + expectedClassName);
    }

    @Test
    void findById() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_EDIT_TEST);
        BudgetDto budgetDto = performSuccessfulFindByIdRequest(budgetId);
        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
    }

    @Test
    void findByIdNotFound() throws Exception {
        final var nonExistingBudgetId = getNonExistingBudgetId();
        RequestConfig requestConfig = RequestConfig.error(FIND_BY_ID, ExceptionType.NOT_FOUND)
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
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_EDIT_TEST);
        var budgetDtos = performSuccessfulFindAllRequest();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(budgetId::equals));
    }


    @Test
    void createSuccess() throws Exception {
        String budgetName = randomBudgetName();
        var requestConfig = RequestConfig.success(CREATE)
            .requestMethod(HttpMethod.POST)
            .requestBody(new BudgetCreateRequestDto(budgetName))
            .build();
        var createdBudgetDto = performAndReturn(requestConfig, new TypeReference<BudgetDto>() {
        });
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        assertEquals(budgetName, createdBudgetDto.getName());

        var budgetDtos = performSuccessfulFindAllRequest();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createdBudgetDto.getId()::equals));
    }

    @Test
    void createEmptyNameBudget() throws Exception {
        var invalidCreateRequestDto = new BudgetCreateRequestDto("");
        var requestConfig = RequestConfig.error(CREATE, ExceptionType.INVALID_PARAMETER)
            .requestMethod(HttpMethod.POST)
            .requestBody(invalidCreateRequestDto)
            .expectResponseBody(true)
            .build();
        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertFieldErrorPresent(responseBody.getData(), BudgetCreateRequestDto.Fields.name, null, null);
    }

    private RequestConfig getAddStatementRequestConfig(Long budgetId, Double expenseAmount) throws Exception {
        StatementCreateDto body = StatementCreateDto.builder()
            .amount(expenseAmount)
            .currency(Currency.EUR)
            .type(StatementType.EXPENSE)
            .date(LocalDateTime.now())
            .build();

        return RequestConfig.success(ADD_STATEMENT)
            .requestMethod(HttpMethod.POST)
            .requestBody(body)
            .requestVariables(List.of(budgetId))
            .build();
    }

    private BudgetDto addStatementToBudget(Long budgetId, Double amount, int initialStatementCount) throws Exception {
        BudgetDto budgetDto = performAndReturn(getAddStatementRequestConfig(budgetId, amount), new TypeReference<>() {
        });

        assertNotNull(budgetDto);
        assertEquals(budgetId, budgetDto.getId());
        assertNotNull(budgetDto.getStatements());
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(amount)));
        assertEquals(initialStatementCount + 1, CollectionUtils.size(budgetDto.getStatements()));
        return budgetDto;
    }

    @Test
    void addStatementToBudgetWithoutInitialStatements() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_STATEMENT_ADDITION_NO_INITIAL_STATEMENTS);
        final var initialStatementCount = performSuccessfulFindByIdRequest(budgetId).getStatements().size();
        addStatementToBudget(budgetId, STATEMENT_FOR_ADD_STATEMENT_AMOUNT, initialStatementCount);
    }

    @Test
    void addStatementToBudgetWithInitialStatements() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS);
        final var initialStatementCount = performSuccessfulFindByIdRequest(budgetId).getStatements().size();
        var budgetDto = addStatementToBudget(budgetId, STATEMENT_FOR_ADD_STATEMENT_AMOUNT, initialStatementCount);

        var statementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());

        final var statementId1 = getStatementIdByIndex(BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS, BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1);
        final var statementId2 = getStatementIdByIndex(BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS, BUDGET_FOR_STATEMENT_ADDITION_MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2);
        assertTrue(CollectionUtils.containsAll(statementIds, Set.of(statementId1, statementId2)));
    }

    @Test
    void addStatementToNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = getNonExistingBudgetId();
        StatementCreateDto body = StatementCreateDto.builder()
            .amount(STATEMENT_FOR_ADD_STATEMENT_AMOUNT)
            .currency(Currency.EUR)
            .type(StatementType.EXPENSE)
            .date(LocalDateTime.now())
            .build();

        var requestConfig = RequestConfig.error(ADD_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.POST)
            .requestBody(body)
            .requestVariables(List.of(nonExistingBudgetId))
            .build();
        ErrorDto<List<FieldErrorDto>> fieldErrorDtoErrorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertFieldErrorPresent(fieldErrorDtoErrorDto.getData(),
            BaseEntity.Fields.id,
            BudgetEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingBudgetId));
    }


    private BudgetDto removeStatementFromBudget(Long budgetId, Long statementId) throws Exception {
        var requestConfig = RequestConfig.success(REMOVE_STATEMENT)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, statementId))
            .expectResponseBody(false)
            .build();
        perform(requestConfig);
        var budgetDto = performSuccessfulFindByIdRequest(budgetId);
        assertNotNull(budgetDto);
        assertNotNull(budgetDto);
        return budgetDto;
    }

    @Test
    void removeStatementWithSingleInitStatement() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_SINGLE_STATEMENT_REMOVE);
        final var statementId1 = getStatementIdByIndex(BUDGET_FOR_SINGLE_STATEMENT_REMOVE, BUDGET_FOR_SINGLE_STATEMENT_REMOVE_STATEMENT_1);
        var budgetDto = removeStatementFromBudget(budgetId, statementId1);
        assertTrue(CollectionUtils.isEmpty(budgetDto.getStatements()));
    }

    @Test
    void removeStatementWithMultipleInitStatements() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE);
        final var statementId1 = getStatementIdByIndex(BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE, BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_SATATEMENT_1);
        final var statementId2 = getStatementIdByIndex(BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE, BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE_SATATEMENT_2);
        var budgetDto = removeStatementFromBudget(budgetId, statementId1);
        var remainingStatementIds = budgetDto.getStatements().stream()
            .map(BaseDto::getId)
            .collect(Collectors.toSet());
        assertTrue(CollectionUtils.containsAll(remainingStatementIds, Set.of(statementId2)));
    }

    @Test
    void removeNonExistsStatement() throws Exception {
        final var budgetId = getBudgetIdByIndex(BUDGET_FOR_MULTIPLE_STATEMENTS_REMOVE);
        final var nonExistingStatementId = getNonExistingStatementId();
        var requestConfig = RequestConfig.error(REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(budgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertFieldErrorPresent(responseBody.getData(),
            BaseEntity.Fields.id,
            StatementEntity.class.getName(),
            fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(nonExistingStatementId));
    }

    @Test
    void removeNonExistsStatementFromNonExistingBudget() throws Exception {
        final var nonExistingBudgetId = getNonExistingBudgetId();
        final var nonExistingStatementId = getNonExistingStatementId();
        var requestConfig = RequestConfig.error(REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
            .requestMethod(HttpMethod.DELETE)
            .requestVariables(List.of(nonExistingBudgetId, nonExistingStatementId))
            .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });
        assertFieldErrorPresent(responseBody.getData(), BaseEntity.Fields.id, BudgetEntity.class.getName());
    }
}
