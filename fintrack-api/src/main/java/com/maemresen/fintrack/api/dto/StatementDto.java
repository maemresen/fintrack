package com.maemresen.fintrack.api.dto;

import com.maemresen.fintrack.api.dto.base.AbstractBaseDatedDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class StatementDto extends AbstractBaseDatedDto {
    private String description;
    private Double amount;
    private StatementType type;
    private Currency currency;
    private LocalDateTime date;
    private String category;
}
