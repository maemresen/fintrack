package com.maemresen.fintrack.api.mapper;

import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.StatementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatementMapper {

    StatementEntity mapToEntity(StatementCreateDto createDto);

    StatementDto mapToDto(StatementEntity entity);
}
