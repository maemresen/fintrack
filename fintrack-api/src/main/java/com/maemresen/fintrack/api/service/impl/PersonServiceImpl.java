package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.aspects.annotations.BusinessMethod;
import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.exceptions.InvalidParameter;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.PersonRepository;
import com.maemresen.fintrack.api.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final StatementMapper statementMapper;

    @BusinessMethod
    @Override
    public Optional<PersonDto> findById(@NotNull Long mockPersonId1) {
        return personRepository.findById(mockPersonId1).map(personMapper::mapToPersonDto);
    }

    @BusinessMethod
    @Override
    public List<PersonDto> findAll() {
        return personRepository.findAll().stream()
                .map(personMapper::mapToPersonDto)
                .toList();
    }

    @BusinessMethod
    @Override
    public PersonDto create(@NotNull PersonCreateRequestDto personCreateRequestDto) {
        PersonEntity personEntity = personMapper.mapToPersonEntity(personCreateRequestDto);
        return personMapper.mapToPersonDto(personRepository.save(personEntity));
    }

    @BusinessMethod
    @Override
    public PersonDto addStatement(@NotNull Long personId, @Valid StatementCreateDto statementCreateDto) {
        PersonEntity personEntity = personRepository.findById(personId).orElseThrow(() -> new InvalidParameter("Person not found"));
        StatementEntity statementEntity = statementMapper.mapToStatementEntity(statementCreateDto);
        personEntity.getStatements().add(statementEntity);
        return personMapper.mapToPersonDto(personRepository.save(personEntity));
    }
}
