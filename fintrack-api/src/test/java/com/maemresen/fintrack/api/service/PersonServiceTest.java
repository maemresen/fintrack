package com.maemresen.fintrack.api.service;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import com.maemresen.fintrack.api.mapper.PersonMapper;
import com.maemresen.fintrack.api.repository.PersonRepsitory;
import com.maemresen.fintrack.api.service.impl.PersonServiceImpl;
import com.maemresen.fintrack.api.util.PersonMockHelper;
import com.maemresen.fintrack.api.util.StatementMockHelper;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private static final Long MOCK_PERSON_ID_1 = 1L;
    private static final String MOCK_PERSON_NAME_1 = "Person 1";
    private static final Long MOCK_STATEMENT_ID_1 = 1L;
    private static final String MOCK_STATEMENT_DESCRIPTION_1 = "Statement 1";
//    private static final Double MOCK_STATEMENT_AMOUNT_1 = 100.0;
//    private static final Currency MOCK_STATEMENT_CURRENCY_1 = Currency.TRY;
//    private static final StatementType MOCK_STATEMENT_TYPE_1 = StatementType.INCOME;
//    private static final LocalDateTime MOCK_STATEMENT_DATE_1 = LocalDateTime.now();
//    private static final String MOCK_STATEMENT_CATEGORY_1 = "Category 1";

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

    @Mock
    private PersonRepsitory personRepsitory;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
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
        MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1 = PersonMockHelper.createMockPersonCreateRequestDtoWithNameAndStatements(MOCK_PERSON_NAME_1, MOCK_STATEMENT_CREATE_DTO_1);
    }

    @Test
    void whenFindByIdShouldReturnPerson() {
        Mockito.when(personRepsitory.findById(MOCK_PERSON_ID_1)).thenReturn(Optional.of(MOCK_PERSON_ENTITY_1));
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        Optional<PersonDto> foundPerson = personService.findById(MOCK_PERSON_ID_1);

        Mockito.verify(personRepsitory).findById(MOCK_PERSON_ID_1);
        Mockito.verify(personMapper).mapToPersonDto(MOCK_PERSON_ENTITY_1);

        Assertions.assertTrue(foundPerson.isPresent());
        Assertions.assertEquals(MOCK_PERSON_DTO_1, foundPerson.get());
    }

    @Test
    void whenFindAllShouldReturnPersons() {
        List<PersonEntity> mockPersonEntities = List.of(MOCK_PERSON_ENTITY_1);
        Mockito.when(personRepsitory.findAll()).thenReturn(mockPersonEntities);
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_1)).thenReturn(MOCK_PERSON_DTO_1);

        List<PersonDto> foundPersons = personService.findAll();

        Mockito.verify(personRepsitory).findAll();
        Mockito.verify(personMapper).mapToPersonDto(MOCK_PERSON_ENTITY_1);

        Assertions.assertEquals(MOCK_PERSON_DTO_1, IterableUtils.get(foundPersons, 0));
    }

    @Test
    void whenSavePersonWithStatementsShouldReturnPerson() {
        Mockito.when(personMapper.mapToPersonEntity(MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1);
        Mockito.when(personRepsitory.save(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1);
        Mockito.when(personMapper.mapToPersonDto(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1)).thenReturn(MOCK_PERSON_DTO_WITH_STATEMENTS_1);

        PersonDto savedPerson = personService.create(MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1);

        Mockito.verify(personMapper).mapToPersonEntity(MOCK_PERSON_CREATE_REQUEST_DTO_WITH_STATEMENTS_1);
        Mockito.verify(personRepsitory).save(MOCK_PERSON_ENTITY_WITHOUT_ID_AND_WITH_STATEMENTS_1);
        Mockito.verify(personMapper).mapToPersonDto(MOCK_PERSON_ENTITY_WITH_STATEMENTS_1);

        Assertions.assertNotNull(savedPerson);
        Assertions.assertEquals(MOCK_PERSON_DTO_WITH_STATEMENTS_1, savedPerson);
    }

}
