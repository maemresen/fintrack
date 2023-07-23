package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    private static final Long MOCK_STATEMENT_ID_2 = 2L;
    private static final String MOCK_STATEMENT_DESCRIPTION_2 = "Statement 2";
    private static final Double MOCK_STATEMENT_AMOUNT_2 = 100.0;
    private static final Currency MOCK_STATEMENT_CURRENCY_2 = Currency.TRY;
    private static final StatementType MOCK_STATEMENT_TYPE_2 = StatementType.INCOME;
    private static final LocalDateTime MOCK_STATEMENT_DATE_2 = LocalDateTime.now();
    private static final String MOCK_STATEMENT_CATEGORY_2 = "Category 1";

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
    }

    @Test
    void whenFindByIdShouldReturnPerson() {
        final PersonEntity existingPersonEntity = PersonMockHelper.createMockPersonEntityWithId(MOCK_PERSON_ID_1);
        final PersonDto personDto = PersonMockHelper.createMockPersonDtoWithId(MOCK_PERSON_ID_1);

        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(existingPersonEntity));
        when(personMapper.mapToPersonDto(existingPersonEntity)).thenReturn(personDto);

        Optional<PersonDto> foundPerson = assertDoesNotThrow(() -> personService.findById(MOCK_PERSON_ID_1));

        verify(personRepository).findById(MOCK_PERSON_ID_1);
        assertTrue(foundPerson.isPresent());
        assertEquals(personDto, foundPerson.get());
    }

    @Test
    void whenFindAllShouldReturnPersons() {
        final PersonEntity existingPersonEntity = PersonMockHelper.createMockPersonEntityWithId(MOCK_PERSON_ID_1);
        final PersonDto personDto = PersonMockHelper.createMockPersonDtoWithId(MOCK_PERSON_ID_1);

        List<PersonEntity> mockPersonEntities = List.of(existingPersonEntity);
        when(personRepository.findAll()).thenReturn(mockPersonEntities);
        when(personMapper.mapToPersonDto(existingPersonEntity)).thenReturn(personDto);

        List<PersonDto> foundPersons = assertDoesNotThrow(() -> personService.findAll());

        verify(personRepository, times(1)).findAll();
        assertEquals(personDto, IterableUtils.get(foundPersons, 0));
    }

    @Test
    void whenSaveWithStatementsShouldReturnPerson() {
        PersonCreateRequestDto personCreateRequestDto = PersonMockHelper.createMockPersonCreateRequestDto(MOCK_PERSON_NAME_1);
        PersonEntity personEntityWithoutId = PersonMockHelper.createMockPersonEntityWithoutId();
        PersonEntity personEntityWithId = PersonMockHelper.createMockPersonEntityWithId(MOCK_PERSON_ID_1);
        PersonDto personDto = PersonMockHelper.createMockPersonDtoWithId(MOCK_PERSON_ID_1);

        when(personMapper.mapToPersonEntity(personCreateRequestDto)).thenReturn(personEntityWithoutId);
        when(personRepository.save(personEntityWithoutId)).thenReturn(personEntityWithId);
        when(personMapper.mapToPersonDto(personEntityWithId)).thenReturn(personDto);

        PersonDto savedPerson = assertDoesNotThrow(() -> personService.create(personCreateRequestDto));

        verify(personRepository, times(1).description("Person entity contains statement must be saved")).save(personEntityWithoutId);
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(personDto, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }

    @Test
    void whenAddStatementShouldReturnPersonWithStatements() {
        final StatementCreateDto statementCreateDto2 = StatementMockHelper.createValidStatementCreateDto(MOCK_STATEMENT_DESCRIPTION_2,
                MOCK_STATEMENT_AMOUNT_2,
                MOCK_STATEMENT_CURRENCY_2,
                MOCK_STATEMENT_TYPE_2,
                MOCK_STATEMENT_DATE_2,
                MOCK_STATEMENT_CATEGORY_2);
        final StatementEntity statementEntity1 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_1);
        final StatementEntity statementEntity2 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_2);
        final StatementEntity statementEntity2WithoutId = StatementMockHelper.createMockStatementEntityWithoutId();

        final PersonEntity existingPersonEntityWithStatement1 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, statementEntity1);
        final PersonEntity newPersonEntityWithStatement1And2 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, statementEntity1, statementEntity2);

        final StatementDto statementDto1 = StatementMockHelper.createMockStatementDto(MOCK_STATEMENT_ID_1);
        final StatementDto statementDto2 = StatementMockHelper.createMockStatementDto(MOCK_STATEMENT_ID_2);
        final PersonDto personDtoWithStatement1And2 = PersonMockHelper.createMockPersonDtoWithIdAndStatements(MOCK_PERSON_ID_1, statementDto1, statementDto2);

        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(existingPersonEntityWithStatement1));
        when(statementMapper.mapToStatementEntity(statementCreateDto2)).thenReturn(statementEntity2WithoutId);
        when(personRepository.save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1, statementEntity2WithoutId))).thenReturn(newPersonEntityWithStatement1And2);
        when(personMapper.mapToPersonDto(newPersonEntityWithStatement1And2)).thenReturn(personDtoWithStatement1And2);

        PersonDto savedPerson = assertDoesNotThrow(() -> personService.addStatement(MOCK_PERSON_ID_1, statementCreateDto2));

        verify(personRepository, times(1).description("Existing Person that statement will be added must be get once from repository")).findById(MOCK_PERSON_ID_1);
        assertTrue(existingPersonEntityWithStatement1.getStatements().contains(statementEntity2WithoutId), "Statement must be added to Person entity");
        verify(personRepository, times(1).description("Updated Person entity must be saved")).save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1, statementEntity2WithoutId));
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(personDtoWithStatement1And2, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }

    @Test
    void whenAddStatementWithNonExistingPersonIdShouldThrowException() {
        final StatementCreateDto statementCreateDto2 = StatementMockHelper.createValidStatementCreateDto(MOCK_STATEMENT_DESCRIPTION_2,
                MOCK_STATEMENT_AMOUNT_2,
                MOCK_STATEMENT_CURRENCY_2,
                MOCK_STATEMENT_TYPE_2,
                MOCK_STATEMENT_DATE_2,
                MOCK_STATEMENT_CATEGORY_2);
        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.empty());
        assertThrows(InvalidParameter.class, () -> personService.addStatement(MOCK_PERSON_ID_1, statementCreateDto2));
        verify(personRepository, never().description("In any error case, save method shouldn't be called")).save(any());
    }

    @Test
    void whenRemoveStatementShouldReturnPersonWithStatements(){
        final StatementEntity statementEntity1 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_1);
        final StatementEntity statementEntity2 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_2);

        final PersonEntity existingPersonEntityWithStatement1And2 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, statementEntity1, statementEntity2);
        final PersonEntity newPersonEntityWithStatement1 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, statementEntity1);

        final PersonDto personDtoWithStatement1 = PersonMockHelper.createMockPersonDtoWithIdAndStatements(MOCK_PERSON_ID_1, StatementMockHelper.createMockStatementDto(MOCK_STATEMENT_ID_1));

        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(existingPersonEntityWithStatement1And2));
        when(personRepository.save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1))).thenReturn(newPersonEntityWithStatement1);
        when(personMapper.mapToPersonDto(newPersonEntityWithStatement1)).thenReturn(personDtoWithStatement1);

        PersonDto savedPerson = assertDoesNotThrow(() -> personService.removeStatement(MOCK_PERSON_ID_1, MOCK_STATEMENT_ID_2));

        verify(personRepository, times(1).description("Existing Person that statement will be removed must be get once from repository")).findById(MOCK_PERSON_ID_1);
        assertFalse(existingPersonEntityWithStatement1And2.getStatements().contains(statementEntity2), "Statement must be removed from Person entity");
        verify(personRepository, times(1).description("Updated Person entity must be saved")).save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1));
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(personDtoWithStatement1, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }

    @Test
    void whenRemoveStatementWithNonExistingPersonIdShouldThrowException() {
        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.empty());

        assertThrows(InvalidParameter.class, () -> personService.removeStatement(MOCK_PERSON_ID_1, MOCK_STATEMENT_ID_2));

        verify(personRepository, never().description("In any error case, save method shouldn't be called")).save(any());
    }

    @Test
    void whenRemoveStatementWithNonExistingStatementIdShouldThrowException() {
        final StatementEntity statementEntity1 = StatementMockHelper.createMockStatementEntityWithId(MOCK_STATEMENT_ID_1);
        final PersonEntity existingPersonEntityWithStatement1 = PersonMockHelper.createMockPersonEntityWithIdAndStatements(MOCK_PERSON_ID_1, statementEntity1);
        final PersonDto personDtoWithStatement1 = PersonMockHelper.createMockPersonDtoWithIdAndStatements(MOCK_PERSON_ID_1, StatementMockHelper.createMockStatementDto(MOCK_STATEMENT_ID_1));

        when(personRepository.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(existingPersonEntityWithStatement1));
        when(personRepository.save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1))).thenReturn(existingPersonEntityWithStatement1);
        when(personMapper.mapToPersonDto(existingPersonEntityWithStatement1)).thenReturn(personDtoWithStatement1);

        PersonDto savedPerson = assertDoesNotThrow(() -> personService.removeStatement(MOCK_PERSON_ID_1, MOCK_STATEMENT_ID_2));

        verify(personRepository, times(1).description("Existing Person that statement will be removed must be get once from repository")).findById(MOCK_PERSON_ID_1);
        assertTrue(existingPersonEntityWithStatement1.getStatements().contains(statementEntity1), "Statement must be removed from Person entity");
        verify(personRepository, times(1).description("Updated Person entity must be saved")).save(PersonMockHelper.getPersonEntityIdAndStatementsArgumentMatcher(MOCK_PERSON_ID_1, statementEntity1));
        assertNotNull(savedPerson, "Saved Person must be returned");
        assertEquals(personDtoWithStatement1, savedPerson, "Saved Person must be mapped to PersonDto and returned");
    }
}
