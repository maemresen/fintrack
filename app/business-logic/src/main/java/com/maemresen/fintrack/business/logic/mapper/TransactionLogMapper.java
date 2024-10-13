package com.maemresen.fintrack.business.logic.mapper;

import com.maemresen.fintrack.business.logic.dto.TransactionLogDto;
import com.maemresen.fintrack.persistence.entity.TransactionLog;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransactionLogMapper {
    TransactionLogDto map(TransactionLog entity);

    TransactionLog map(TransactionLogDto dto);
}
