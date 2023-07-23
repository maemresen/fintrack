package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.base.AbstractBaseTest;
import com.maemresen.fintrack.api.entity.Person;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test-h2")
class PersonServiceTest extends AbstractBaseTest {

    protected Long MOCK_PERSON_ID_1;
    protected Person MOCK_PERSON_1;

    @BeforeEach
    void init(){
        MOCK_PERSON_1 = new Person();
        MOCK_PERSON_1.setId(MOCK_PERSON_ID_1);
    }

    @MockBean
    private PersonRepsitory personRepsitory;

    @Autowired
    private PersonService personService;

    @Test
    void findUsers(){
        Mockito.when(personRepsitory.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_1));

        Optional<Person> foundPerson = personService.findById(MOCK_PERSON_ID_1);

        Assertions.assertTrue(foundPerson.isPresent());
        Assertions.assertEquals(MOCK_PERSON_1, foundPerson.get());
    }
}
