package com.maemresen.fintrack.api.dto;

import com.maemresen.fintrack.api.dto.base.BaseDatedDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import lombok.AllArgsConstructor;
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
public class StatementDto extends BaseDatedDto {
    private String description;
    private Double amount;
    private StatementType type;
    private Currency currency;
    private LocalDateTime date;
    private String category;
}
