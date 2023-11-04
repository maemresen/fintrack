package com.maemresen.fintrack.webservice.business.repository;


import com.maemresen.fintrack.webservice.business.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

}
