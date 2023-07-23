package com.maemresen.fintrack.api.util;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.assertj.core.util.Sets;

import java.util.Set;

import static org.mockito.ArgumentMatchers.argThat;

@UtilityClass
public class PersonMockHelper {
    public static PersonEntity createMockPersonEntityWithoutId() {
        return createMockPersonEntityWithId(null);
    }

    public static PersonEntity createMockPersonEntityWithId(Long id) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        return personEntity;
    }

    public static PersonEntity createMockPersonEntityWithIdAndStatements(Long id, StatementEntity... statements) {
        PersonEntity personEntity = createMockPersonEntityWithId(id);

        if (statements != null) {
            personEntity.setStatements(SetUtils.hashSet(statements));
        }
        return personEntity;
    }

    public static PersonDto createMockPersonDtoWithId(Long id) {
        return createMockPersonDtoWithIdAndStatements(id);
    }

    public static PersonDto createMockPersonDtoWithIdAndStatements(Long id, StatementDto... statements) {
        PersonDto personDto = new PersonDto();
        personDto.setId(id);
        personDto.setStatements(SetUtils.hashSet(statements));
        return personDto;
    }

    public static PersonCreateRequestDto createMockPersonCreateRequestDto(String name) {
        PersonCreateRequestDto personCreateRequestDto = new PersonCreateRequestDto();
        personCreateRequestDto.setName(name);
        return personCreateRequestDto;
    }


    public static PersonEntity getPersonEntityIdAndStatementsArgumentMatcher(Long id, StatementEntity...statements) {
        return argThat(arg -> {
            if(!arg.getId().equals(id)){
                return false;
            }

            if(statements == null){
                return arg.getStatements() == null;
            }

            if (CollectionUtils.size(arg.getStatements()) != statements.length) {
                return false;
            }

            return arg.getStatements().containsAll(Set.of(statements));
        });
    }
}
