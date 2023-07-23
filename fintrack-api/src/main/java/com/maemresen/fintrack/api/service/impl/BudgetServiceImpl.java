package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.aspects.annotations.BusinessMethod;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.exceptions.InvalidParameter;
import com.maemresen.fintrack.api.mapper.BudgetMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import com.maemresen.fintrack.api.service.BudgetService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Validated
@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final StatementMapper statementMapper;

    @BusinessMethod
    @Override
    public Optional<BudgetDto> findById(@NotNull Long mockBudgetId1) {
        return budgetRepository.findById(mockBudgetId1).map(budgetMapper::mapToBudgetDto);
    }

    @BusinessMethod
    @Override
    public List<BudgetDto> findAll() {
        return budgetRepository.findAll().stream()
                .map(budgetMapper::mapToBudgetDto)
                .toList();
    }

    @BusinessMethod
    @Transactional
    @Override
    public BudgetDto create(@Valid BudgetCreateRequestDto budgetCreateRequestDto) {
        BudgetEntity budgetEntity = budgetMapper.mapToBudgetEntity(budgetCreateRequestDto);
        return budgetMapper.mapToBudgetDto(budgetRepository.save(budgetEntity));
    }

    @BusinessMethod
    @Transactional
    @Override
    public BudgetDto addStatement(@NotNull(message = "Budget Id cannot be null") Long budgetId, @Valid StatementCreateDto statementCreateDto) {
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId).orElseThrow(() -> new InvalidParameter("Budget not found"));
        StatementEntity statementEntity = statementMapper.mapToStatementEntity(statementCreateDto);
        Set<StatementEntity> statements = budgetEntity.getStatements();
        if(statements == null){
            statements = new HashSet<>();
            budgetEntity.setStatements(statements);
        }

        statements.add(statementEntity);
        statementEntity.setBudget(budgetEntity);

        return budgetMapper.mapToBudgetDto(budgetRepository.save(budgetEntity));
    }

    @BusinessMethod
    @Transactional
    @Override
    public BudgetDto removeStatement(@NotNull(message = "Budget Id cannot be null") Long budgetId, @NotNull(message = "Statement Id cannot be null") Long statementId) {
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId).orElseThrow(() -> new InvalidParameter("Budget not found"));

        Set<StatementEntity> statements = budgetEntity.getStatements();
        if (statements != null) {
            statements.removeIf(statementEntity -> statementEntity.getId().equals(statementId));
        }

        return budgetMapper.mapToBudgetDto(budgetRepository.save(budgetEntity));
    }
}
