package com.maemresen.fintrack.api.integration.test.service;

import com.maemresen.fintrack.api.dto.BudgetCreateDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
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
import org.apache.commons.collections4.IterableUtils;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @DisplayName("Add Statement to Budget with initial Statements")
    @Test
    void addStatementToBudgetWithInitialStatements(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final BudgetEntity budgetEntity = budgets.get(BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final long budgetId = budgetEntity.getId();
        final List<StatementEntity> statements = budgetEntity.getStatements();
        final int statementCount = statements.size();
        final StatementEntity initialStatementEntity1 = statements.get(BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final StatementEntity initialStatementEntity2 = statements.get(BudgetItConstants.AddStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        final StatementCreateDto validStatementCreateDto = getValidStatementCreateDto();

        final BudgetDto updatedBudgetDto = budgetService.addStatement(budgetId, validStatementCreateDto);
        assertNotNull(updatedBudgetDto);

        final Set<StatementDto> updatedStatements = updatedBudgetDto.getStatements();
        assertEquals(statementCount + 1, CollectionUtils.size(updatedStatements));

        assertTrue(IterableUtils.matchesAny(
            updatedStatements,
            object -> object.getDescription().equals(initialStatementEntity1.getDescription())
        ));
        assertTrue(IterableUtils.matchesAny(
            updatedStatements,
            object -> object.getDescription().equals(initialStatementEntity2.getDescription())
        ));
        assertTrue(IterableUtils.matchesAny(
            updatedStatements,
            object -> object.getDescription().equals(validStatementCreateDto.getDescription())
        ));
    }

    @DisplayName("Remove Statement from Budget")
    @Test
    void removeStatementFromBudget(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final BudgetEntity budgetEntity = budgets.get(BudgetItConstants.RemoveStatement.SINGLE_INITIAL_STATEMENT_BUDGET_INDEX);
        final List<StatementEntity> statements = budgetEntity.getStatements();
        final StatementEntity statementEntity = statements.get(BudgetItConstants.RemoveStatement.SINGLE_INITIAL_STATEMENT_STATEMENT_INDEX);

        final BudgetDto updatedBudgetDto = budgetService.removeStatement(budgetEntity.getId(), statementEntity.getId());
        assertNotNull(updatedBudgetDto);

        final Set<StatementDto> updatedStatements = updatedBudgetDto.getStatements();
        assertTrue(CollectionUtils.isEmpty(updatedStatements));
    }

    @DisplayName("Add Statement to Budget with initial Statements")
    @Test
    void removeStatementToBudgetWithInitialStatements(@TestData.Name(BUDGET_DATA) List<BudgetEntity> budgets) {
        final BudgetEntity budgetEntity = budgets.get(BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_BUDGET_INDEX);
        final long budgetId = budgetEntity.getId();
        final List<StatementEntity> statements = budgetEntity.getStatements();
        final int statementCount = statements.size();

        final StatementEntity initialStatementEntity1 = statements.get(BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_1_INDEX);
        final long initialStatementEntityId1 = initialStatementEntity1.getId();

        final StatementEntity initialStatementEntity2 = statements.get(BudgetItConstants.RemoveStatement.MULTIPLE_INITIAL_STATEMENTS_STATEMENT_2_INDEX);
        final long initialStatementEntityId2 = initialStatementEntity2.getId();

        final BudgetDto updatedBudgetDto = budgetService.removeStatement(budgetId, initialStatementEntityId1);
        assertNotNull(updatedBudgetDto);

        final Set<StatementDto> updatedStatements = updatedBudgetDto.getStatements();
        assertEquals(statementCount - 1, CollectionUtils.size(updatedStatements));

        assertFalse(IterableUtils.matchesAny(
            updatedStatements,
            object -> object.getId().equals(initialStatementEntityId1)
        ));
        assertTrue(IterableUtils.matchesAny(
            updatedStatements,
            object -> object.getId().equals(initialStatementEntityId2)
        ));
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

    private StatementCreateDto getValidStatementCreateDto() {
        final StatementCreateDto statementCreateDto = new StatementCreateDto();
        statementCreateDto.setDescription("ADD_VALID_STATEMENT_TEST_DESC");
        statementCreateDto.setAmount(100D);
        statementCreateDto.setCurrency(Currency.EUR);
        statementCreateDto.setType(StatementType.INCOME);
        statementCreateDto.setDate(LocalDateTime.now());
        return statementCreateDto;
    }

    private void assertEqualsStatementDto(final StatementCreateDto createDto, final StatementDto dto) {
        assertAll(
            () -> assertEquals(createDto.getDescription(), dto.getDescription()),
            () -> assertEquals(createDto.getAmount(), dto.getAmount()),
            () -> assertEquals(createDto.getCurrency(), dto.getCurrency()),
            () -> assertEquals(createDto.getType(), dto.getType()),
            () -> assertEquals(createDto.getDate(), dto.getDate())
        );
    }

    private long getNonExistingBudgetId(final List<BudgetEntity> budgets) {
        return budgets.stream()
            .map(BaseEntity::getId)
            .max(Long::compareTo)
            .orElse(0L) + 1L;
    }
}
