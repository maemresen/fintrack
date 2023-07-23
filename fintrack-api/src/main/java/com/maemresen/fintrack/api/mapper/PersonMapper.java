package com.maemresen.fintrack.api.mapper;

import com.maemresen.fintrack.api.dto.PersonCreateRequestDto;
import com.maemresen.fintrack.api.dto.PersonDto;
import com.maemresen.fintrack.api.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {StatementMapper.class})
public interface PersonMapper {
    PersonDto mapToPersonDto(PersonEntity personEntity);

    PersonEntity mapToPersonEntity(PersonCreateRequestDto personCreateRequestDto);
}
