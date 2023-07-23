package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test-h2")
class PersonServiceTest {

    protected Long MOCK_PERSON_ID_1;
    protected PersonEntity MOCK_PERSON_ENTITY_1;
    protected PersonDto MOCK_PERSON_DTO_1;

    @BeforeEach
    void init(){
        MOCK_PERSON_ENTITY_1 = new PersonEntity();
        MOCK_PERSON_ENTITY_1.setId(MOCK_PERSON_ID_1);

        MOCK_PERSON_DTO_1 = new PersonDto();
        MOCK_PERSON_DTO_1.setId(MOCK_PERSON_ID_1);
    }

    @MockBean
    private PersonRepsitory personRepsitory;

    @MockBean
    private PersonMapper personMapper;

    @Autowired
    private PersonService personService;

    @Test
    void findById(){
        Mockito.when(personRepsitory.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_ENTITY_1));
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        Optional<PersonDto> foundPerson = personService.findById(MOCK_PERSON_ID_1);

        Assertions.assertTrue(foundPerson.isPresent());
        Assertions.assertEquals(MOCK_PERSON_DTO_1, foundPerson.get());
    }
}
