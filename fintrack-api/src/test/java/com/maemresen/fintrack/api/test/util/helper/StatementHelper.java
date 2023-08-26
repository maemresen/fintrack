package com.maemresen.fintrack.api.test.util.helper;

import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.StatementEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class StatementHelper {
    public static StatementEntity createStatementEntityWithoutId() {
        return createStatementEntityWithId(null);
    }

    public static StatementEntity createStatementEntityWithId(Long id) {
        StatementEntity statementEntity = new StatementEntity();
        statementEntity.setId(id);
        return statementEntity;
    }

    public static StatementDto createStatementDto(Long id) {
        StatementDto statementDto = new StatementDto();
        statementDto.setId(id);
        return statementDto;
    }

    public static StatementCreateDto createInvalidStatementCreateDto(String description) {
        return createValidStatementCreateDto(description, null, null, null, null, null);
    }

    public static StatementCreateDto createValidStatementCreateDto(String description,
                                                                   Double amount,
                                                                   Currency currency,
                                                                   StatementType type,
                                                                   LocalDateTime date,
                                                                   String category
    ) {
        StatementCreateDto statementCreateDto = new StatementCreateDto();
        statementCreateDto.setDescription(description);
        statementCreateDto.setAmount(amount);
        statementCreateDto.setCurrency(currency);
        statementCreateDto.setType(type);
        statementCreateDto.setDate(date);
        statementCreateDto.setCategory(category);
        return statementCreateDto;
    }

}
