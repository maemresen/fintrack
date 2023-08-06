
package com.maemresen.fintrack.api.rest.budget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.base.AbstractBaseRestWithDbIT;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.util.RequestBuilder;
import com.maemresen.fintrack.api.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

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

        var request = RequestBuilder.withoutBody()
                .method(HttpMethod.GET)
                .uri(FIND_BY_ID)
                .variables(List.of(budgetId))
                .build();
        var response = performSuccessfulApiCall(request)
                .andReturn()
                .getResponse();
        return readResponse(response, new TypeReference<>() {
        });
    }

    protected BudgetDto performSuccessfulCreateRequest(BudgetCreateRequestDto createRequestDto) throws Exception {
        var request = RequestBuilder.withBody(objectMapper)
                .method(HttpMethod.POST)
                .uri(CREATE)
                .body(createRequestDto)
                .build();
        var response = performSuccessfulApiCall(request)
                .andReturn()
                .getResponse();

        return readResponse(response, new TypeReference<>() {
        });
    }

    protected List<BudgetDto> performSuccessfulFindAllRequest() throws Exception {
        var request = httpRequest(HttpMethod.GET, FIND_ALL);
        var response = performSuccessfulApiCall(request)
                .andReturn()
                .getResponse();
        return readResponse(response, new TypeReference<>() {
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
        var createRequest = httpRequestWihBody(HttpMethod.POST, CREATE, new BudgetCreateRequestDto(""));
        performBadRequestApiCall(createRequest);
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

        var createRequest = httpRequestWihBody(HttpMethod.POST, ADD_STATEMENT, body, TEST_BUDGET_1_ID);
        ResultActions response = performSuccessfulApiCall(createRequest);
        BudgetDto budgetDto = readResponse(response.andReturn().getResponse(), new TypeReference<>() {
        });
        assertNotNull(budgetDto);
        assertEquals(TEST_BUDGET_1_ID, budgetDto.getId());
        assertTrue(CollectionUtils.isNotEmpty(budgetDto.getStatements()));
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(expenseAmount)));
    }

    @Test
    @Order(6)
    void removeStatement() throws Exception {
        var removeStatementRequest = httpRequest(HttpMethod.DELETE, REMOVE_STATEMENT, TEST_BUDGET_1_ID, TEST_STATEMENT_1_ID);
        performSuccessfulApiCall(removeStatementRequest, false);
    }
}