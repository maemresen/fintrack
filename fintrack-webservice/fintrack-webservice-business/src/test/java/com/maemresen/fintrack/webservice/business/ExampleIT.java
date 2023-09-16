package com.maemresen.fintrack.webservice.business;

import com.maemresen.fintrack.webservice.business.base.AbstractBaseJpaTest;
import com.maemresen.fintrack.webservice.business.entity.BudgetEntity;
import com.maemresen.fintrack.webservice.business.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExampleIT extends AbstractBaseJpaTest {

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
