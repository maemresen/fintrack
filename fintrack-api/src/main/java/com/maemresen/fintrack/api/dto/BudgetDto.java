package com.maemresen.fintrack.api.dto;

import com.maemresen.fintrack.api.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BudgetDto extends BaseDto {
    private String name;
    private Set<StatementDto> statements = new HashSet<>();
}
