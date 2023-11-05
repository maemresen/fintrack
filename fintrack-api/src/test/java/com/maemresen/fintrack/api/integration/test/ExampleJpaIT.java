package com.maemresen.fintrack.api.integration.test;

import com.maemresen.fintrack.api.entity.BudgetEntity;
import com.maemresen.fintrack.api.integration.test.base.AbstractBaseJpaTest;
import com.maemresen.fintrack.api.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExampleJpaIT extends AbstractBaseJpaTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    void contextLoads() {

        final var name = "test";

        final var budget = new BudgetEntity();
        budget.setName(name);

        final var savedBudget = budgetRepository.save(budget);
        assertNotNull(savedBudget);

        final var findBudget = budgetRepository.findById(savedBudget.getId());
        assertTrue(findBudget.isPresent());
        assertThat(findBudget.get().getName()).isEqualTo(name);
    }
}
