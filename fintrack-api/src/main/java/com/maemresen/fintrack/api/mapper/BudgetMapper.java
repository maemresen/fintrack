package com.maemresen.fintrack.api.mapper;

import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.entity.BudgetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {StatementMapper.class})
public interface BudgetMapper {
    BudgetDto mapToBudgetDto(BudgetEntity budgetEntity);

    BudgetEntity mapToBudgetEntity(BudgetCreateRequestDto budgetCreateRequestDto);
}
