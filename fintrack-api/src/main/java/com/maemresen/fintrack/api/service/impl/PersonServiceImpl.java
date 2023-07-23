package com.maemresen.fintrack.api.service.impl;

import com.maemresen.fintrack.api.entity.Person;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import com.maemresen.fintrack.api.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepsitory personRepsitory;

    @Override
    public Optional<Person> findById(Long mockPersonId1) {
        return personRepsitory.findById(mockPersonId1);
    }
}
