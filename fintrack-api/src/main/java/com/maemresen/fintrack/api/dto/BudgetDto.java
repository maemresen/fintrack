package com.maemresen.fintrack.api.dto;

import com.maemresen.fintrack.api.dto.base.AbstractBaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BudgetDto extends AbstractBaseDto {
    private String name;
    private Set<StatementDto> statements = new HashSet<>();
}
