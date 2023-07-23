package com.maemresen.fintrack.api.repository;

import com.maemresen.fintrack.api.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepsitory extends JpaRepository<PersonEntity, Long> {
}
