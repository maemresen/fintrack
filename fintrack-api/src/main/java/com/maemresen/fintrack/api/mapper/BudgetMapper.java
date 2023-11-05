package com.maemresen.fintrack.api.mapper;

import com.maemresen.fintrack.api.dto.BudgetCreateDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = StatementMapper.class)
public interface BudgetMapper {

    BudgetEntity mapToEntity(BudgetCreateDto createDto);

    BudgetDto mapToDto(BudgetEntity entity);
}
