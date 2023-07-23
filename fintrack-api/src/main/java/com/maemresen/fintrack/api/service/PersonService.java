package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonDto;

import java.util.Optional;

public interface PersonService {
    Optional<PersonDto> findById(Long mockPersonId1);
}
