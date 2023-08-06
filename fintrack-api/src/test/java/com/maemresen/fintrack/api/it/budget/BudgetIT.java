package com.maemresen.fintrack.api.it.budget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.it.base.AbstractBaseRestIT;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext
class BudgetIT extends AbstractBaseRestIT {

    private static final String BASE_URI = "/budget";
    private static final String CREATE = BASE_URI;
    private static final String FIND_ALL = BASE_URI;
    private static final String ADD_STATEMENT = BASE_URI + "/{budgetId}/statement";

    private static final String BUDGET_1_NAME = "Budget 1";
    private static final String BUDGET_2_NAME = "Budget 2";

    protected BudgetDto performSuccessfulCreateBudgetRequest(BudgetCreateRequestDto createRequestDto) throws Exception {
        var createRequest = post(CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsByte(createRequestDto));

        var createResponse = performSuccessfulApiCall(createRequest)
                .andReturn()
                .getResponse();

        return readResponse(createResponse, new TypeReference<>() {
        });
    }

    protected List<BudgetDto> performSuccessfulFindAllBudgetRequest() throws Exception {
        var findAllRequest = get(FIND_ALL).contentType(MediaType.APPLICATION_JSON);
        var findAllResponse = performSuccessfulApiCall(findAllRequest)
                .andReturn()
                .getResponse();
        return readResponse(findAllResponse, new TypeReference<>() {
        });
    }

    @Test
    @Order(1)
    void createSuccess() throws Exception {
        var createdBudgetDto = performSuccessfulCreateBudgetRequest(new BudgetCreateRequestDto(BUDGET_1_NAME));
        assertNotNull(createdBudgetDto);
        assertNotNull(createdBudgetDto.getId());
        assertEquals(BUDGET_1_NAME, createdBudgetDto.getName());

    }

    @Test
    @Order(2)
    void findAll() throws Exception {
        var createResponse = performSuccessfulCreateBudgetRequest(new BudgetCreateRequestDto(BUDGET_2_NAME));
        var budgetDtos = performSuccessfulFindAllBudgetRequest();
        assertTrue(CollectionUtils.isNotEmpty(budgetDtos));
        assertTrue(budgetDtos.stream().map(BudgetDto::getId).anyMatch(createResponse.getId()::equals));
    }

    @Test
    @Order(3)
    void createEmptyNameBudget() throws Exception {
        var createRequest = post(CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsByte(new BudgetCreateRequestDto("")));
        performBadRequestApiCall(createRequest);
    }

    @Test
    @Order(4)
    void addIncomeStatement() throws Exception {
        Long randomBudgetId = performSuccessfulFindAllBudgetRequest().stream().map(BudgetDto::getId).findAny().orElseThrow();
        var exponseAmount = 100D;
        var createRequest = post(ADD_STATEMENT, randomBudgetId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsByte(StatementCreateDto.builder()
                        .amount(exponseAmount)
                        .currency(Currency.EUR)
                        .type(StatementType.EXPENSE)
                        .date(LocalDateTime.now())
                        .build()));
        ResultActions response = performSuccessfulApiCall(createRequest);
        BudgetDto budgetDto = readResponse(response.andReturn().getResponse(), new TypeReference<>() {
        });
        assertNotNull(budgetDto);
        assertEquals(randomBudgetId, budgetDto.getId());
        assertTrue(CollectionUtils.isNotEmpty(budgetDto.getStatements()));
        assertTrue(budgetDto.getStatements().stream().anyMatch(statementDto -> statementDto.getAmount().equals(exponseAmount)));
    }
}
