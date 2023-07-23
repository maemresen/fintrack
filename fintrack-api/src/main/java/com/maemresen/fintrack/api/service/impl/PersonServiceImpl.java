package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import com.maemresen.fintrack.api.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepsitory personRepsitory;
    private final PersonMapper personMapper;

    @Override
    public Optional<PersonDto> findById(Long mockPersonId1) {
        return personRepsitory.findById(mockPersonId1).map(personMapper::mapToPersonDto);
    }

    @Override
    public List<PersonDto> findAll() {
        return personRepsitory.findAll().stream()
                .map(personMapper::mapToPersonDto)
                .toList();
    }

    @Override
    public PersonDto create(PersonCreateRequestDto personCreateRequestDto) {
        PersonEntity personEntity = personMapper.mapToPersonEntity(personCreateRequestDto);
        return personMapper.mapToPersonDto(personRepsitory.save(personEntity));
    }
}
