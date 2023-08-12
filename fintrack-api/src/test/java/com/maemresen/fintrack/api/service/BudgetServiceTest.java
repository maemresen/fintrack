package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.exceptions.NotFoundException;
import com.maemresen.fintrack.api.mapper.BudgetMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import com.maemresen.fintrack.api.service.impl.BudgetServiceImpl;
import com.maemresen.fintrack.api.util.BudgetMockHelper;
import com.maemresen.fintrack.api.util.StatementHelper;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles({"test-h2"})
class BudgetServiceTest {

    private static final Long BUDGET_ID_1 = 1L;
    private static final String BUDGET_NAME_1 = "Budget 1";
    private static final Long STATEMENT_ID_1 = 1L;
    private static final Long STATEMENT_ID_2 = 2L;
    private static final String STATEMENT_DESCRIPTION_2 = "Statement 2";
    private static final Double STATEMENT_AMOUNT_1 = 100.0;
    private static final Double STATEMENT_AMOUNT_2 = 200.0;
    private static final Double STATEMENT_AMOUNT_3 = 50.0;
    private static final Currency STATEMENT_CURRENCY_2 = Currency.TRY;
    private static final StatementType STATEMENT_TYPE_2 = StatementType.INCOME;
    private static final LocalDateTime STATEMENT_DATE_2 = LocalDateTime.now();
    private static final String STATEMENT_CATEGORY_2 = "Category 1";
    private static final int MONTH_1 = Month.AUGUST.getValue();

    @MockBean
    private BudgetRepository budgetRepository;

    @MockBean
    private BudgetMapper budgetMapper;

    @MockBean
    private StatementMapper statementMapper;

    @Autowired
    private BudgetServiceImpl budgetService;

    @Test
    void whenFindByIdShouldReturnBudget() {
        final BudgetEntity existingBudgetEntity = BudgetMockHelper.createMockBudgetEntityWithId(BUDGET_ID_1);
        final BudgetDto budgetDto = BudgetMockHelper.createMockBudgetDtoWithId(BUDGET_ID_1);

        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.of(existingBudgetEntity));
        when(budgetMapper.mapToBudgetDto(existingBudgetEntity)).thenReturn(budgetDto);

        Optional<BudgetDto> foundBudget = assertDoesNotThrow(() -> budgetService.findById(BUDGET_ID_1));

        verify(budgetRepository).findById(BUDGET_ID_1);
        assertTrue(foundBudget.isPresent());
        assertEquals(budgetDto, foundBudget.get());
    }

    @Test
    void whenFindAllShouldReturnBudgets() {
        final BudgetEntity existingBudgetEntity = BudgetMockHelper.createMockBudgetEntityWithId(BUDGET_ID_1);
        final BudgetDto budgetDto = BudgetMockHelper.createMockBudgetDtoWithId(BUDGET_ID_1);

        List<BudgetEntity> mockBudgetEntities = List.of(existingBudgetEntity);
        when(budgetRepository.findAll()).thenReturn(mockBudgetEntities);
        when(budgetMapper.mapToBudgetDto(existingBudgetEntity)).thenReturn(budgetDto);

        List<BudgetDto> foundBudgets = assertDoesNotThrow(() -> budgetService.findAll());

        verify(budgetRepository, times(1)).findAll();
        assertEquals(budgetDto, IterableUtils.get(foundBudgets, 0));
    }

    @Test
    void whenSaveWithStatementsShouldReturnBudget() {
        BudgetCreateRequestDto budgetCreateRequestDto = BudgetMockHelper.createMockBudgetCreateRequestDto(BUDGET_NAME_1);
        BudgetEntity budgetEntityWithoutId = BudgetMockHelper.createMockBudgetEntityWithoutId();
        BudgetEntity budgetEntityWithId = BudgetMockHelper.createMockBudgetEntityWithId(BUDGET_ID_1);
        BudgetDto budgetDto = BudgetMockHelper.createMockBudgetDtoWithId(BUDGET_ID_1);

        when(budgetMapper.mapToBudgetEntity(budgetCreateRequestDto)).thenReturn(budgetEntityWithoutId);
        when(budgetRepository.save(budgetEntityWithoutId)).thenReturn(budgetEntityWithId);
        when(budgetMapper.mapToBudgetDto(budgetEntityWithId)).thenReturn(budgetDto);

        BudgetDto savedBudget = assertDoesNotThrow(() -> budgetService.create(budgetCreateRequestDto));

        verify(budgetRepository, times(1).description("Budget entity contains statement must be saved")).save(budgetEntityWithoutId);
        assertNotNull(savedBudget, "Saved Budget must be returned");
        assertEquals(budgetDto, savedBudget, "Saved Budget must be mapped to BudgetDto and returned");
    }

    @Test
    void whenAddStatementShouldReturnBudgetWithStatements() {
        final StatementCreateDto statementCreateDto2 = StatementHelper.createValidStatementCreateDto(STATEMENT_DESCRIPTION_2, STATEMENT_AMOUNT_2, STATEMENT_CURRENCY_2, STATEMENT_TYPE_2, STATEMENT_DATE_2, STATEMENT_CATEGORY_2);
        final StatementEntity statementEntity1 = StatementHelper.createStatementEntityWithId(STATEMENT_ID_1);
        final StatementEntity statementEntity2 = StatementHelper.createStatementEntityWithId(STATEMENT_ID_2);
        final StatementEntity statementEntity2WithoutId = StatementHelper.createStatementEntityWithoutId();

        final BudgetEntity existingBudgetEntityWithStatement1 = BudgetMockHelper.createMockBudgetEntityWithIdAndStatements(BUDGET_ID_1, statementEntity1);
        final BudgetEntity newBudgetEntityWithStatement1And2 = BudgetMockHelper.createMockBudgetEntityWithIdAndStatements(BUDGET_ID_1, statementEntity1, statementEntity2);

        final StatementDto statementDto1 = StatementHelper.createStatementDto(STATEMENT_ID_1);
        final StatementDto statementDto2 = StatementHelper.createStatementDto(STATEMENT_ID_2);
        final BudgetDto budgetDtoWithStatement1And2 = BudgetMockHelper.createMockBudgetDtoWithIdAndStatements(BUDGET_ID_1, statementDto1, statementDto2);

        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.of(existingBudgetEntityWithStatement1));
        when(statementMapper.mapToStatementEntity(statementCreateDto2)).thenReturn(statementEntity2WithoutId);
        when(budgetRepository.save(BudgetMockHelper.getBudgetEntityIdAndStatementsArgumentMatcher(BUDGET_ID_1, statementEntity1, statementEntity2WithoutId))).thenReturn(newBudgetEntityWithStatement1And2);
        when(budgetMapper.mapToBudgetDto(newBudgetEntityWithStatement1And2)).thenReturn(budgetDtoWithStatement1And2);

        BudgetDto savedBudget = assertDoesNotThrow(() -> budgetService.addStatement(BUDGET_ID_1, statementCreateDto2));

        verify(budgetRepository, times(1).description("Existing Budget that statement will be added must be get once from repository")).findById(BUDGET_ID_1);
        assertTrue(existingBudgetEntityWithStatement1.getStatements().contains(statementEntity2WithoutId), "Statement must be added to Budget entity");
        verify(budgetRepository, times(1).description("Updated Budget entity must be saved")).save(BudgetMockHelper.getBudgetEntityIdAndStatementsArgumentMatcher(BUDGET_ID_1, statementEntity1, statementEntity2WithoutId));
        assertNotNull(savedBudget, "Saved Budget must be returned");
        assertEquals(budgetDtoWithStatement1And2, savedBudget, "Saved Budget must be mapped to BudgetDto and returned");
    }

    @Test
    void whenAddStatementWithNonExistingBudgetIdShouldThrowException() {
        final StatementCreateDto statementCreateDto2 = StatementHelper.createValidStatementCreateDto(STATEMENT_DESCRIPTION_2, STATEMENT_AMOUNT_2, STATEMENT_CURRENCY_2, STATEMENT_TYPE_2, STATEMENT_DATE_2, STATEMENT_CATEGORY_2);
        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> budgetService.addStatement(BUDGET_ID_1, statementCreateDto2));
        verify(budgetRepository, never().description("In any error case, save method shouldn't be called")).save(any());
    }

    @Test
    void whenRemoveStatementShouldReturnBudgetWithStatements() {
        final StatementEntity statementEntity1 = StatementHelper.createStatementEntityWithId(STATEMENT_ID_1);
        final StatementEntity statementEntity2 = StatementHelper.createStatementEntityWithId(STATEMENT_ID_2);

        final BudgetEntity existingBudgetEntityWithStatement1And2 = BudgetMockHelper.createMockBudgetEntityWithIdAndStatements(BUDGET_ID_1, statementEntity1, statementEntity2);
        final BudgetEntity newBudgetEntityWithStatement1 = BudgetMockHelper.createMockBudgetEntityWithIdAndStatements(BUDGET_ID_1, statementEntity1);

        final BudgetDto budgetDtoWithStatement1 = BudgetMockHelper.createMockBudgetDtoWithIdAndStatements(BUDGET_ID_1, StatementHelper.createStatementDto(STATEMENT_ID_1));

        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.of(existingBudgetEntityWithStatement1And2));
        when(budgetRepository.save(BudgetMockHelper.getBudgetEntityIdAndStatementsArgumentMatcher(BUDGET_ID_1, statementEntity1))).thenReturn(newBudgetEntityWithStatement1);
        when(budgetMapper.mapToBudgetDto(newBudgetEntityWithStatement1)).thenReturn(budgetDtoWithStatement1);

        BudgetDto savedBudget = assertDoesNotThrow(() -> budgetService.removeStatement(BUDGET_ID_1, STATEMENT_ID_2));

        verify(budgetRepository, times(1).description("Existing Budget that statement will be removed must be get once from repository")).findById(BUDGET_ID_1);
        assertFalse(existingBudgetEntityWithStatement1And2.getStatements().contains(statementEntity2), "Statement must be removed from Budget entity");
        verify(budgetRepository, times(1).description("Updated Budget entity must be saved")).save(BudgetMockHelper.getBudgetEntityIdAndStatementsArgumentMatcher(BUDGET_ID_1, statementEntity1));
        assertNotNull(savedBudget, "Saved Budget must be returned");
        assertEquals(budgetDtoWithStatement1, savedBudget, "Saved Budget must be mapped to BudgetDto and returned");
    }

    @Test
    void whenRemoveStatementWithNonExistingBudgetIdShouldThrowException() {
        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.removeStatement(BUDGET_ID_1, STATEMENT_ID_2));

        verify(budgetRepository, never().description("In any error case, save method shouldn't be called")).save(any());
    }

    @Test
    void whenRemoveStatementWithNonExistingStatementIdShouldThrowException() {
        final StatementEntity statementEntity1 = StatementHelper.createStatementEntityWithId(STATEMENT_ID_1);
        final BudgetEntity existingBudgetEntityWithStatement1 = BudgetMockHelper.createMockBudgetEntityWithIdAndStatements(BUDGET_ID_1, statementEntity1);
        final BudgetDto budgetDtoWithStatement1 = BudgetMockHelper.createMockBudgetDtoWithIdAndStatements(BUDGET_ID_1, StatementHelper.createStatementDto(STATEMENT_ID_1));

        when(budgetRepository.findById(BUDGET_ID_1)).thenReturn(Optional.of(existingBudgetEntityWithStatement1));
        when(budgetRepository.save(BudgetMockHelper.getBudgetEntityIdAndStatementsArgumentMatcher(BUDGET_ID_1, statementEntity1))).thenReturn(existingBudgetEntityWithStatement1);
        when(budgetMapper.mapToBudgetDto(existingBudgetEntityWithStatement1)).thenReturn(budgetDtoWithStatement1);

        assertThrows(NotFoundException.class, () -> budgetService.removeStatement(BUDGET_ID_1, STATEMENT_ID_2));
    }
}
