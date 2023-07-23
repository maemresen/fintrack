package com.maemresen.fintrack.api.util;

import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.StatementEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatementMockHelper {
    public static StatementEntity createMockStatementEntityWithId(Long id){
        StatementEntity statementEntity = new StatementEntity();
        statementEntity.setId(id);
        return statementEntity;
    }

    public static StatementDto createMockStatementDto(Long id){
        StatementDto statementDto = new StatementDto();
        statementDto.setId(id);
        return statementDto;
    }

    public static StatementCreateDto createMockStatementCreateDtoWithDescription(String description){
        StatementCreateDto statementCreateDto = new StatementCreateDto();
        statementCreateDto.setDescription(description);
        return statementCreateDto;
    }
}
