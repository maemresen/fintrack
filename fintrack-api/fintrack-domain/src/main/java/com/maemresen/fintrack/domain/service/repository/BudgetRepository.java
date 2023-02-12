package com.maemresen.fintrack.domain.service.repository;

import com.maemresen.fintrack.domain.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}


