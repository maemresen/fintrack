package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.aspects.annotations.BusinessMethod;
import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.BudgetReportDto;
import com.maemresen.fintrack.api.dto.FieldErrorDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.exceptions.NotFoundException;
import com.maemresen.fintrack.api.mapper.BudgetMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import com.maemresen.fintrack.api.service.BudgetService;
import com.maemresen.fintrack.api.repository.StatementRepository;
import com.maemresen.fintrack.api.utils.helper.DateUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Validated
@Service
public class BudgetServiceImpl implements BudgetService {

    public static final String BUDGET_NOT_FOUND = "Budget not found";
    public static final String STATEMENT_NOT_FOUND = "Statement not found";

    private final BudgetRepository budgetRepository;
    private final StatementRepository statementRepository;
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
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
            .orElseThrow(() -> new NotFoundException(BUDGET_NOT_FOUND, List.of(
                FieldErrorDto.withFieldClass(BudgetEntity.class, BaseEntity.Fields.id, BUDGET_NOT_FOUND, budgetId)
            )));
        StatementEntity statementEntity = statementMapper.mapToStatementEntity(statementCreateDto);
        var statements = budgetEntity.getStatements();
        if (statements == null) {
            statements = new ArrayList<>();
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
        BudgetEntity budgetEntity = budgetRepository.findById(budgetId)
            .orElseThrow(() -> new NotFoundException(BUDGET_NOT_FOUND, List.of(
                FieldErrorDto.withFieldClass(BudgetEntity.class, BaseEntity.Fields.id, BUDGET_NOT_FOUND, budgetId)
            )));

        var statements = budgetEntity.getStatements();
        if (!CollectionUtils.emptyIfNull(statements).removeIf(statementEntity -> statementEntity.getId().equals(statementId))) {
            throw new NotFoundException(STATEMENT_NOT_FOUND, List.of(
                FieldErrorDto.withFieldClass(StatementEntity.class, BaseEntity.Fields.id, STATEMENT_NOT_FOUND, statementId)
            ));
        }

        return budgetMapper.mapToBudgetDto(budgetRepository.save(budgetEntity));
    }

    @BusinessMethod
    @Override
    public List<BudgetReportDto> monthlyReportForYear(@NotNull(message = "Budget Id cannot be null") Long budgetId, @NotNull(message = "Year cannot be null") int year) {

        if (!budgetRepository.existsById(budgetId)) {
            throw new NotFoundException(BUDGET_NOT_FOUND, List.of(
                FieldErrorDto.withFieldClass(BudgetEntity.class, BaseEntity.Fields.id, BUDGET_NOT_FOUND, budgetId)
            ));
        }

        var lowerBound = DateUtils.getFirstDayOfYear(year);
        var upperBound = DateUtils.getLastDayOfYear(year);
        var statements = statementRepository.findByBudgetIdAndDateBetween(budgetId, lowerBound, upperBound);

        if (CollectionUtils.isEmpty(statements)) {
            return List.of();
        }

        return statements.stream()
            .collect(Collectors.groupingBy(
                statementEntity -> statementEntity.getDate().getMonth(),
                Collectors.summingDouble(statementEntity -> statementEntity.getIsIncome() ? statementEntity.getAmount() : -statementEntity.getAmount())
            )).entrySet().stream()
            .map(entry -> new BudgetReportDto(entry.getKey(), entry.getValue()))
            .toList();
    }
}
