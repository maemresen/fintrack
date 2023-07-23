package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    Optional<PersonDto> findById(@NotNull Long mockPersonId1);
    List<PersonDto> findAll();
    PersonDto create(@Valid PersonCreateRequestDto personCreateRequestDto);
    PersonDto addStatement(@NotNull(message = "Person Id cannot be null") Long personId, @Valid StatementCreateDto statementCreateDto);
    PersonDto removeStatement(@NotNull(message = "Person Id cannot be null") Long personId, @NotNull(message = "Statement Id cannot be null") Long statementId);
}
