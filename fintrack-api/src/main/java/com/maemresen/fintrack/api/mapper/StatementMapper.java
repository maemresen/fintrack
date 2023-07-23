package com.maemresen.fintrack.api.mapper;

import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.StatementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatementMapper {
    StatementDto mapToStatementDto(StatementEntity statementEntity);

    StatementEntity  mapToStatementEntity(StatementCreateDto statementCreateDto);
}
