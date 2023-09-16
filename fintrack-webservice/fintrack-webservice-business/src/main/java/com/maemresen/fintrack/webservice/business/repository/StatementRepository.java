package com.maemresen.fintrack.webservice.business.repository;


import com.maemresen.fintrack.webservice.business.entity.StatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, Long> {

    List<StatementEntity> findByBudgetIdAndDateBetween(Long budgetId, LocalDateTime startDate, LocalDateTime endDate);
}
