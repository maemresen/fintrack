package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import com.maemresen.fintrack.api.service.impl.PersonServiceImpl;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private Long MOCK_PERSON_ID_1;
    private PersonEntity MOCK_PERSON_ENTITY_1;
    private PersonDto MOCK_PERSON_DTO_1;
    private PersonCreateRequestDto MOCK_PERSON_CREATE_REQUEST_DTO_1;
//    private List<S>

    @Mock
    private PersonRepsitory personRepsitory;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void init(){
        MOCK_PERSON_ENTITY_1 = new PersonEntity();
        MOCK_PERSON_ENTITY_1.setId(MOCK_PERSON_ID_1);

        MOCK_PERSON_DTO_1 = new PersonDto();
        MOCK_PERSON_DTO_1.setId(MOCK_PERSON_ID_1);
    }

    @Test
    void findById(){
        Mockito.when(personRepsitory.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_ENTITY_1));
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        Optional<PersonDto> foundPerson = personService.findById(MOCK_PERSON_ID_1);

        Assertions.assertTrue(foundPerson.isPresent());
        Assertions.assertEquals(MOCK_PERSON_DTO_1, foundPerson.get());
    }

    @Test
    void findAll(){
        List<PersonEntity> mockPersonEntities = List.of(MOCK_PERSON_ENTITY_1);
        Mockito.when(personRepsitory.findAll()).thenReturn(mockPersonEntities);
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        List<PersonDto> foundPersons = personService.findAll();

        Assertions.assertEquals(MOCK_PERSON_DTO_1, IterableUtils.get(foundPersons, 0));
    }
//
//    @Test
//    void
}
