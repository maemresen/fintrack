package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<PersonDto> findById(Long mockPersonId1);

    List<PersonDto> findAll();

    PersonDto create(PersonCreateRequestDto personCreateRequestDto);
}
