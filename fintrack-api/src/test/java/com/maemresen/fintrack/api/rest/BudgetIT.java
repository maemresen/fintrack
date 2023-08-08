package com.maemresen.fintrack.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.base.AbstractBaseRestWithDbIT;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.ErrorDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.util.StringHelper;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
class BudgetIT extends AbstractBaseRestWithDbIT {

    private static final String BASE_URI = "/budget";

    private static final String CREATE = BASE_URI;
    private static final String FIND_BY_ID = BASE_URI + "/{budgetId}";
    private static final String FIND_ALL = BASE_URI;
    private static final String ADD_STATEMENT = BASE_URI + "/{budgetId}/statement";
    private static final String REMOVE_STATEMENT = BASE_URI + "/{budgetId}/statement/{statementId}";

    private static final Long TEST_BUDGET_1_ID = 1L;
    private static final Long NON_EXISTING_BUDGET_1_ID = 100L;
    private static final Long TEST_STATEMENT_1_ID = 1L;

    private BudgetDto performSuccessfulFindByIdRequest(Long budgetId) throws Exception {
        var requestConfig = RequestConfig.success(FIND_BY_ID)
                .requestMethod(HttpMethod.GET)
                .requestVariables(List.of(budgetId)).build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    private BudgetDto performSuccessfulCreateRequest(BudgetCreateRequestDto createRequestDto) throws Exception {
        var requestConfig = RequestConfig.success(CREATE)
                .requestMethod(HttpMethod.POST)
                .requestBody(createRequestDto)
                .build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    private List<BudgetDto> performSuccessfulFindAllRequest() throws Exception {
        var requestConfig = RequestConfig.success(FIND_ALL)
                .requestMethod(HttpMethod.GET)
                .build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    @Test
    @Order(0)
    void findById() throws Exception {
        var budgetDto = performSuccessfulFindByIdRequest(TEST_BUDGET_1_ID);
        assertNotNull(budgetDto);
        assertEquals(TEST_BUDGET_1_ID, budgetDto.getId());
    }


    @Test
    @Order(0)
    void findByIdNotFound() throws Exception {
        RequestConfig requestConfig = RequestConfig.error(FIND_BY_ID, ExceptionType.NOT_FOUND)
                .requestMethod(HttpMethod.GET)
                .requestVariables(List.of(NON_EXISTING_BUDGET_1_ID))
                .build();

        ErrorDto<Long> errorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });
        assertNotNull(errorDto);
        assertEquals(NON_EXISTING_BUDGET_1_ID, errorDto.getData());
    }


    @Test
    @Order(1)
    void findAll() throws Exception {
        var budgetDtos = performSuccessfulFindAllRequest();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(TEST_BUDGET_1_ID::equals));
    }

    @Test
    @Order(2)
    void createSuccess() throws Exception {
        String budgetName = StringHelper.randomString("Budget");
        var budgetCreateRequestDto = new BudgetCreateRequestDto(budgetName);
        var createdBudgetDto = performSuccessfulCreateRequest(budgetCreateRequestDto);
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        assertEquals(budgetName, createdBudgetDto.getName());

        var budgetDtos = performSuccessfulFindAllRequest();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createdBudgetDto.getId()::equals));
    }

    @Test
    @Order(3)
    void createEmptyNameBudget() throws Exception {
        var invalidCreateRequestDto = new BudgetCreateRequestDto("");
        var requestConfig = RequestConfig.error(CREATE, ExceptionType.INVALID_PARAMETER)
                .requestMethod(HttpMethod.POST)
                .requestBody(invalidCreateRequestDto)
                .expectResponseBody(true)
                .build();
        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(responseBody);

        var fieldValidationErrors = responseBody.getData();
        assertTrue(CollectionUtils.isNotEmpty(fieldValidationErrors));

        var fieldValidationError = fieldValidationErrors.get(0);
        assertEquals(BudgetCreateRequestDto.Fields.name, fieldValidationError.getField());
    }

    @Test
    @Order(4)
    void addIncomeStatement() throws Exception {
        var expenseAmount = 100D;
        StatementCreateDto body = StatementCreateDto.builder()
                .amount(expenseAmount)
                .currency(Currency.EUR)
                .type(StatementType.EXPENSE)
                .date(LocalDateTime.now())
                .build();

        var requestConfig = RequestConfig.success(ADD_STATEMENT)
                .requestMethod(HttpMethod.POST)
                .requestBody(body)
                .requestVariables(List.of(TEST_BUDGET_1_ID))
                .build();
        BudgetDto budgetDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(budgetDto);
        assertEquals(TEST_BUDGET_1_ID, budgetDto.getId());
        assertTrue(CollectionUtils.isNotEmpty(budgetDto.getStatements()));
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(expenseAmount)));
    }


    @Test
    @Order(4)
    void addIncomeStatementToNonExistingBudget() throws Exception {
        var expenseAmount = 100D;
        StatementCreateDto body = StatementCreateDto.builder()
                .amount(expenseAmount)
                .currency(Currency.EUR)
                .type(StatementType.EXPENSE)
                .date(LocalDateTime.now())
                .build();

        var requestConfig = RequestConfig.error(ADD_STATEMENT, ExceptionType.NOT_FOUND)
                .requestMethod(HttpMethod.POST)
                .requestBody(body)
                .requestVariables(List.of(NON_EXISTING_BUDGET_1_ID))
                .build();
        ErrorDto<List<FieldErrorDto>> fieldErrorDtoErrorDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(fieldErrorDtoErrorDto);
        Optional<FieldErrorDto> optionalFieldErrorDto = CollectionUtils.emptyIfNull(fieldErrorDtoErrorDto.getData()).stream()
                .filter(fieldErrorDto -> fieldErrorDto.getFieldClass().equals(BudgetEntity.class.getName()))
                .filter(fieldErrorDto -> fieldErrorDto.getField().equals(BaseEntity.Fields.id))
                .filter(fieldErrorDto -> fieldErrorDto.getLongRejectedValue().equals(NON_EXISTING_BUDGET_1_ID))
                .findFirst();

        if (optionalFieldErrorDto.isEmpty()) {
            fail("Budget field error not found");
        }
    }

    @Test
    @Order(6)
    void removeStatement() throws Exception {
        var requestConfig = RequestConfig.success(REMOVE_STATEMENT)
                .requestMethod(HttpMethod.DELETE)
                .requestVariables(List.of(TEST_BUDGET_1_ID, TEST_STATEMENT_1_ID))
                .expectResponseBody(false)
                .build();
        perform(requestConfig);
    }

    @Test
    @Order(7)
    void removeNonExistsStatement() throws Exception {
        var requestConfig = RequestConfig.error(REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
                .requestMethod(HttpMethod.DELETE)
                .requestVariables(List.of(TEST_BUDGET_1_ID, TEST_STATEMENT_1_ID))
                .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(responseBody);

        var data = responseBody.getData();
        assertTrue(CollectionUtils.isNotEmpty(data));

        var statementIdFieldError = data.stream()
                .filter(fieldErrorDto -> fieldErrorDto.getFieldClass().equals(StatementEntity.class.getName()))
                .filter(fieldErrorDto -> fieldErrorDto.getField().equals(BaseEntity.Fields.id))
                .findFirst();
        assertTrue(statementIdFieldError.isPresent(), "Statement id field error not found");
    }

    @Test
    @Order(8)
    void removeNonExistsStatementFromNonExistingBudget() throws Exception {
        var requestConfig = RequestConfig.error(REMOVE_STATEMENT, ExceptionType.NOT_FOUND)
                .requestMethod(HttpMethod.DELETE)
                .requestVariables(List.of(NON_EXISTING_BUDGET_1_ID, TEST_STATEMENT_1_ID))
                .build();

        ErrorDto<List<FieldErrorDto>> responseBody = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(responseBody);

        var data = responseBody.getData();
        assertTrue(CollectionUtils.isNotEmpty(data));

        var budgetIdFieldError = data.stream()
                .filter(fieldErrorDto -> fieldErrorDto.getFieldClass().equals(BudgetEntity.class.getName()))
                .filter(fieldErrorDto -> fieldErrorDto.getField().equals(BaseEntity.Fields.id))
                .findFirst();
        assertTrue(budgetIdFieldError.isPresent(), "Budget id field error not found");
    }
}