
package com.maemresen.fintrack.api.rest.budget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.base.AbstractBaseRestWithDbIT;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.util.RequestConfig;
import com.maemresen.fintrack.api.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final Long TEST_STATEMENT_1_ID = 1L;
    private static final Long TEST_STATEMENT_2_ID = 2L;

    protected BudgetDto performSuccessfulFindByIdRequest(Long budgetId) throws Exception {
        var requestConfig = RequestConfig.get(FIND_BY_ID).variables(List.of(budgetId)).build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    protected BudgetDto performSuccessfulCreateRequest(BudgetCreateRequestDto createRequestDto) throws Exception {
        var requestConfig = RequestConfig.postWithBody(CREATE, createRequestDto).expectResponse(true).build();
        return performAndReturn(requestConfig, new TypeReference<>() {
        });
    }

    protected List<BudgetDto> performSuccessfulFindAllRequest() throws Exception {
        var requestConfig = RequestConfig.get(FIND_ALL).build();
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
        var requestConfig = RequestConfig.postWithBody(CREATE, invalidCreateRequestDto)
                .expectResponse(false)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
        perform(requestConfig);
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

        var requestConfig = RequestConfig.postWithBody(ADD_STATEMENT, body).variables(List.of(TEST_BUDGET_1_ID)).build();
        BudgetDto budgetDto = performAndReturn(requestConfig, new TypeReference<>() {
        });

        assertNotNull(budgetDto);
        assertEquals(TEST_BUDGET_1_ID, budgetDto.getId());
        assertTrue(CollectionUtils.isNotEmpty(budgetDto.getStatements()));
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(expenseAmount)));
    }

    @Test
    @Order(6)
    void removeStatement() throws Exception {
        var requestConfig = RequestConfig.delete(REMOVE_STATEMENT)
                .variables(List.of(TEST_BUDGET_1_ID, TEST_STATEMENT_1_ID))
                .expectResponse(false)
                .build();
        perform(requestConfig);
    }

    @Test
    @Order(7)
    void removeNonExistsStatement() throws Exception {
        var requestConfig = RequestConfig.delete(REMOVE_STATEMENT)
                .variables(List.of(TEST_BUDGET_1_ID, TEST_STATEMENT_1_ID))
                .expectResponse(false)
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        perform(requestConfig);
    }
}