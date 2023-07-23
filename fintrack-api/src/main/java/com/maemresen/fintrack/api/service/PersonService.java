package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.entity.Person;

import java.util.Optional;

public interface PersonService {
    Optional<Person> findById(Long mockPersonId1);
}
