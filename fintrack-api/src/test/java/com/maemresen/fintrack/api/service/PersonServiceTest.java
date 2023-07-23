package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.exceptions.BusinessException;
import com.maemresen.fintrack.api.exceptions.InvalidParameter;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.mapper.StatementMapper;
import com.maemresen.fintrack.api.repository.PersonRepository;
import com.maemresen.fintrack.api.service.impl.PersonServiceImpl;
import com.maemresen.fintrack.api.util.PersonMockHelper;
import com.maemresen.fintrack.api.util.StatementMockHelper;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceTest {

    private static final Long MOCK_PERSON_ID_1 = 1L;
    private static final String MOCK_PERSON_NAME_1 = "Person 1";
    private static final Long MOCK_STATEMENT_ID_1 = 1L;
    private static final String MOCK_STATEMENT_DESCRIPTION_1 = "Statement 1";
    private static final Double MOCK_STATEMENT_AMOUNT_1 = 100.0;
    private static final Currency MOCK_STATEMENT_CURRENCY_1 = Currency.TRY;
    private static final StatementType MOCK_STATEMENT_TYPE_1 = StatementType.INCOME;
    private static final LocalDateTime MOCK_STATEMENT_DATE_1 = LocalDateTime.now();
    private static final String MOCK_STATEMENT_CATEGORY_1 = "Category 1";


    private PersonEntity MOCK_PERSON_ENTITY_1;
    private PersonEntity MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1;
    private PersonEntity MOCK_PERSON_ENTITY_WITH_STATEMENTS_1;
    private StatementEntity MOCK_STATEMENT_ENTITY_1;
    private StatementEntity MOCK_STATEMENT_ENTITY_WITHOUT_ID_1;

    private PersonDto MOCK_PERSON_DTO_1;
    private PersonDto MOCK_PERSON_DTO_WITH_STATEMENTS_1;
    private PersonCreateRequestDto MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1;

    private StatementDto MOCK_STATEMENT_DTO_1;
    private StatementCreateDto MOCK_STATEMENT_CREATE_DTO_1;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private StatementMapper statementMapper;

    @Autowired
    private PersonServiceImpl personService;

    @BeforeEach
    void init() {
        MOCK_PERSON_ENTITY_1 = PersonMockHelper.createMockPersonEntityWithId(MOCK_PERSON_ID_1);

        MOCK_STATEMENT_ENTITY_1 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_1);
        MOCK_PERSON_ENTITY_WITH_STATEMENTS_1 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, MOCK_STATEMENT_ENTITY_1);

        MOCK_STATEMENT_ENTITY_WITHOUT_ID_1 = StatementMockHelper.createMockStatementEntityWithId(null);
        MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(null, MOCK_STATEMENT_ENTITY_WITHOUT_ID_1);

        MOCK_PERSON_DTO_1 = PersonMockHelper.createMockPersonDtoWithId(MOCK_PERSON_ID_1);

        MOCK_STATEMENT_DTO_1 = StatementMockHelper.createMockStatementDto(MOCK_STATEMENT_ID_1);
        MOCK_PERSON_DTO_WITH_STATEMENTS_1 = PersonMockHelper.createMockPersonDtoWithIdAndStatements(MOCK_PERSON_ID_1, MOCK_STATEMENT_DTO_1);

        MOCK_STATEMENT_CREATE_DTO_1 = StatementMockHelper.createMockStatementCreateDtoWithDescription(MOCK_STATEMENT_DESCRIPTION_1);
        MOCK_STATEMENT_CREATE_DTO_1.setAmount(MOCK_STATEMENT_AMOUNT_1);
        MOCK_STATEMENT_CREATE_DTO_1.setCurrency(MOCK_STATEMENT_CURRENCY_1);
        MOCK_STATEMENT_CREATE_DTO_1.setType(MOCK_STATEMENT_TYPE_1);
        MOCK_STATEMENT_CREATE_DTO_1.setDate(MOCK_STATEMENT_DATE_1);
        MOCK_STATEMENT_CREATE_DTO_1.setCategory(MOCK_STATEMENT_CATEGORY_1);

        MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1 = PersonMockHelper.createMockPersonCreateRequestDtoWithNameAndStatements(MOCK_PERSON_NAME_1, MOCK_STATEMENT_CREATE_DTO_1);

    }

    @Test
    void whenFindByIdShouldReturnPerson() {
        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_ENTITY_1));
        when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        Optional<PersonDto> foundPerson = assertDoesNotThrow(() -> personService.findById(MOCK_PERSON_ID_1));

        verify(personRepository).findById(MOCK_PERSON_ID_1);
        assertTrue(foundPerson.isPresent());
        assertEquals(MOCK_PERSON_DTO_1, foundPerson.get());
    }

    @Test
    void whenFindAllShouldReturnPersons() {
        List<PersonEntity> mockPersonEntities = List.of(MOCK_PERSON_ENTITY_1);
        when(personRepository.findAll()).thenReturn(mockPersonEntities);
        when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        List<PersonDto> foundPersons = assertDoesNotThrow(() -> personService.findAll());

        verify(personRepository, times(1)).findAll();
        assertEquals(MOCK_PERSON_DTO_1, IterableUtils.get(foundPersons, 0));
    }

    @Test
    void whenSaveWithStatementsShouldReturnPerson() {
        when(personMapper.mapToPersonEntity(MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1);
        when(personRepository.save(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1);
        when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_DTO_WITH_STATEMENTS_1);

        PersonDto savedPerson = assertDoesNotThrow(()-> personService.create(MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1));

        verify(personRepository, times(1).description("Person entity contains statement must be saved")).save(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1);
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(MOCK_PERSON_DTO_WITH_STATEMENTS_1, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }

    @Test
    void whenAddStatementShouldReturnPersonWithStatements() {
        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_ENTITY_1));
        when(statementMapper.mapToStatementEntity(MOCK_STATEMENT_CREATE_DTO_1)).thenReturn(MOCK_STATEMENT_ENTITY_WITHOUT_ID_1);
        when(personRepository.save(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1);
        when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_DTO_WITH_STATEMENTS_1);

        PersonDto savedPerson = assertDoesNotThrow(() -> personService.addStatement(MOCK_PERSON_ID_1, MOCK_STATEMENT_CREATE_DTO_1));

        verify(personRepository, times(1).description("Existing Person that statement will be added must be get once from repository")).findById(MOCK_PERSON_ID_1);
        assertTrue(MOCK_PERSON_ENTITY_1.getStatements().contains(MOCK_STATEMENT_ENTITY_WITHOUT_ID_1), "Statement must be added to Person entity");
        verify(personRepository, times(1).description("Updated Person entity must be saved")).save(MOCK_PERSON_ENTITY_1);
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(MOCK_PERSON_DTO_WITH_STATEMENTS_1, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }

    @Test
    void whenAddStatementWithNonExistingPersonIdShouldThrowException() {
        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.empty());
        assertThrows(InvalidParameter.class, () -> personService.addStatement(MOCK_PERSON_ID_1, MOCK_STATEMENT_CREATE_DTO_1));
        verify(personRepository, never().description("In any error case, save method shouldn't be called")).save(any());
    }
}
