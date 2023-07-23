package com.maemresen.fintrack.api.dto;

import com.maemresen.fintrack.api.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDto extends BaseDto {
    private String name;
    private Set<StatementDto> statements;
}
