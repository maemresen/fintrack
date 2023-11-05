package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.BudgetCreateDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface BudgetService {
    Optional<BudgetDto> findById(long createdBudgetId);

    BudgetDto create(@Valid BudgetCreateDto budgetCreateDto);

    BudgetDto addStatement(@NotNull Long id, @Valid StatementCreateDto statementCreateDto);

    BudgetDto removeStatement(@NotNull Long id, @NotNull Long id1);
}
