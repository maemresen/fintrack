package com.maemresen.fintrack.api.util;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class PersonMockHelper {

    public static PersonEntity createMockPersonEntityWithId(Long id) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        return personEntity;
    }

    public static PersonEntity createMockPersonEntityWithIdAndStatements(Long id, StatementEntity... statements) {
        PersonEntity personEntity = createMockPersonEntityWithId(id);

        if (statements != null) {
            personEntity.setStatements(Set.of(statements));
        }
        return personEntity;
    }

    public static PersonDto createMockPersonDtoWithId(Long id) {
        return createMockPersonDtoWithIdAndStatements(id);
    }

    public static PersonDto createMockPersonDtoWithIdAndStatements(Long id, StatementDto... statements) {
        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        personDto.setStatements(Set.of(statements));
        return personDto;
    }

    public static PersonCreateRequestDto createMockPersonCreateRequestDtoWithNameAndStatements(String name, StatementCreateDto... statements) {
        PersonCreateRequestDto personCreateRequestDto = new PersonCreateRequestDto();
        personCreateRequestDto.setName(name);

        if (statements != null) {
            personCreateRequestDto.setStatements(Set.of(statements));
        }

        return personCreateRequestDto;
    }

}
