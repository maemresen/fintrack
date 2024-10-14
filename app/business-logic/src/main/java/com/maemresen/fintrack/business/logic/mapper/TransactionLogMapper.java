package com.maemresen.fintrack.business.logic.mapper;

import com.maemresen.fintrack.business.logic.dto.TransactionLogCreateRequestDto;
import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.persistence.entity.TransactionLog;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true)
)
public interface TransactionLogMapper {
    TransactionLogDto map(TransactionLog entity);

    TransactionLog map(TransactionLogDto dto);

    @Mapping(ignore = true, target = "id")
    TransactionLog map(TransactionLogCreateRequestDto createRequestDto);
}
