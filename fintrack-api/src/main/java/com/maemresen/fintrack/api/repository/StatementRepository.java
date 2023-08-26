package com.maemresen.fintrack.api.repository;

import com.maemresen.fintrack.api.entity.StatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Long> {

    List<StatementEntity> findByBudgetIdAndDateBetween(Long budgetId, LocalDateTime startDate, LocalDateTime endDate);
}
