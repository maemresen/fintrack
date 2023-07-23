package com.maemresen.fintrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonCreateRequestDto {
    private String name;
    private Set<StatementCreateDto> statements;
}
