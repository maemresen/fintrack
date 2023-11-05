package com.maemresen.fintrack.api.integration.test.service;

import com.maemresen.fintrack.api.dto.BudgetCreateDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.exception.NotFoundException;
import com.maemresen.fintrack.api.integration.test.base.AbstractBaseIT;
import com.maemresen.fintrack.api.integration.test.config.TestData;
import com.maemresen.fintrack.api.integration.test.util.BudgetEntityDataLoader;
import com.maemresen.fintrack.api.integration.test.util.constant.BudgetItConstants;
import com.maemresen.fintrack.api.service.BudgetService;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestData(sources = {
    @TestData.Source(
        name = BudgetServiceIT.BUDGET_DATA,
        path = "data/budget-service-it-data.json",
        loader = BudgetEntityDataLoader.class
    )
})
@Transactional
class BudgetServiceIT extends AbstractBaseIT {

    protected static final String BUDGET_DATA = "budgets";

    @Autowired
    private BudgetService budgetService;

    @DisplayName("Create valid Budget")
    @Test
    void createValidBudget() {
        final long createdBudgetId = createBudget("VALID_BUDGET").getId();
        final Optional<BudgetDto> budgetDtoById = budgetService.findById(createdBudgetId);
        assertTrue(budgetDtoById.isPresent());
    }

    @DisplayName("Creating invalid Budget")
    @ParameterizedTest(name = "Creating Budget with '{0}' name")
    @NullAndEmptySource
    void createBudgetWithBlankName(final String name) {
        final BudgetCreateDto budgetCreateDto = new BudgetCreateDto();
        budgetCreateDto.setName(name);

        assertThrows(ConstraintViolationException.class, () -> budgetService.create(budgetCreateDto));
    }

    @DisplayName("Add valid Statement to Budget")
    @Test
    void addValidStatementToInitiallyEmptyBudget(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final BudgetEntity addValidStatementBudget = budgets.get(BudgetItConstants.AddStatement.NO_INITIAL_STATEMENTS_BUDGET_INDEX);

        final StatementCreateDto statementCreateDto = new StatementCreateDto();
        statementCreateDto.setDescription("ADD_VALID_STATEMENT_TEST_DESC");
        statementCreateDto.setAmount(100D);
        statementCreateDto.setCurrency(Currency.EUR);
        statementCreateDto.setType(StatementType.INCOME);
        statementCreateDto.setDate(LocalDateTime.now());

        final BudgetDto updatedBudgetDto = budgetService.addStatement(addValidStatementBudget.getId(), statementCreateDto);

        assertNotNull(updatedBudgetDto);

        final Set<StatementDto> statements = updatedBudgetDto.getStatements();
        assertEquals(1, CollectionUtils.size(statements));
        assertEqualsStatementDto(statementCreateDto, statements.iterator().next());
    }

    @DisplayName("Add non-existing Statement to Budget")
    @Test
    void addStatementToNonExistingBudget(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final BudgetEntity addValidStatementBudget = budgets.get(BudgetItConstants.AddStatement.NO_INITIAL_STATEMENTS_BUDGET_INDEX);
        final StatementCreateDto statementCreateDto = getValidStatementCreateDto();

        final BudgetDto updatedBudgetDto = budgetService.addStatement(addValidStatementBudget.getId(), statementCreateDto);

        assertNotNull(updatedBudgetDto);

        final Set<StatementDto> statements = updatedBudgetDto.getStatements();
        assertEquals(1, CollectionUtils.size(statements));
        assertEqualsStatementDto(statementCreateDto, statements.iterator().next());
    }


    @DisplayName("Add invalid Statement to Budget")
    @Test
    void addInvalidStatementToInitiallyEmptyBudget(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final StatementCreateDto validStatementCreateDto = getValidStatementCreateDto();
        final long nonExistingBudgetId = getNonExistingBudgetId(budgets);
        final NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            budgetService.addStatement(nonExistingBudgetId, validStatementCreateDto);
        });

        assertEquals(nonExistingBudgetId, notFoundException.getData());
    }

    private BudgetDto createBudget(final String budgetName) {
        final BudgetCreateDto budgetCreateDto = new BudgetCreateDto();
        budgetCreateDto.setName(budgetName);

        final BudgetDto createdBudgetDto = budgetService.create(budgetCreateDto);
        assertNotNull(createdBudgetDto);

        final String createdBudgetName = createdBudgetDto.getName();
        assertEquals(budgetName, createdBudgetName);

        return createdBudgetDto;
    }

    private StatementCreateDto getValidStatementCreateDto(){
        final StatementCreateDto statementCreateDto = new StatementCreateDto();
        statementCreateDto.setDescription("ADD_VALID_STATEMENT_TEST_DESC");
        statementCreateDto.setAmount(100D);
        statementCreateDto.setCurrency(Currency.EUR);
        statementCreateDto.setType(StatementType.INCOME);
        statementCreateDto.setDate(LocalDateTime.now());
        return statementCreateDto;
    }

    private void assertEqualsStatementDto(final StatementCreateDto createDto, final StatementDto dto) {
        assertEquals(createDto.getDescription(), dto.getDescription());
        assertEquals(createDto.getAmount(), dto.getAmount());
        assertEquals(createDto.getCurrency(), dto.getCurrency());
        assertEquals(createDto.getType(), dto.getType());
        assertEquals(createDto.getDate(), dto.getDate());
    }

    private long getNonExistingBudgetId(final List<BudgetEntity> budgets){
        return budgets.stream()
            .map(BaseEntity::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }
}
