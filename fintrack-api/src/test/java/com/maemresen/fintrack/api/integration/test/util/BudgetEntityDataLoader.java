package com.maemresen.fintrack.api.integration.test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.integration.test.base.AbstractBaseDataLoader;
import com.maemresen.fintrack.api.mapper.BudgetMapper;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BudgetEntityDataLoader extends AbstractBaseDataLoader<List<BudgetEntity>, List<BudgetEntity>> {
    public BudgetEntityDataLoader() {
        super(new TypeReference<>() {
        });
    }

    @Override
    protected List<BudgetEntity> loadData(ApplicationContext applicationContext, List<BudgetEntity> data) {
        final BudgetMapper budgetMapper = applicationContext.getBean(BudgetMapper.class);
        final BudgetRepository budgetRepository = applicationContext.getBean(BudgetRepository.class);
        return data.stream().map(budget -> {
            budget.getStatements().forEach(statement -> statement.setBudget(budget));
            return budgetRepository.save(budget);
        }).toList();
    }
}
