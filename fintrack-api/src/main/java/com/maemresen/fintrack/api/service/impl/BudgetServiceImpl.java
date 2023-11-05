package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.dto.BudgetCreateDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.exception.NotFoundException;
import com.maemresen.fintrack.api.mapper.BudgetMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import com.maemresen.fintrack.api.service.BudgetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final StatementMapper statementMapper;

    @Override
    public Optional<BudgetDto> findById(long createdBudgetId) {
        return budgetRepository.findById(createdBudgetId)
            .map(budgetMapper::mapToDto);
    }

    @Override
    public BudgetDto create(@Valid BudgetCreateDto budgetCreateDto) {
        final BudgetEntity budgetEntity = budgetMapper.mapToEntity(budgetCreateDto);
        return budgetMapper.mapToDto(budgetRepository.save(budgetEntity));
    }

    @Override
    public BudgetDto addStatement(@NotNull Long id, @Valid StatementCreateDto statementCreateDto) {
        final StatementEntity statementEntity = statementMapper.mapToEntity(statementCreateDto);

        final BudgetEntity budgetEntity = budgetRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Budget not found.", id));

        List<StatementEntity> statements = budgetEntity.getStatements();
        if(statements == null) {
            statements = new ArrayList<>();
        }

        statements.add(statementEntity);
        statementEntity.setBudget(budgetEntity);
        return budgetMapper.mapToDto(budgetRepository.save(budgetEntity));
    }
}
