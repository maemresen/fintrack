package com.maemresen.fintrack.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonCreateRequestDto {

    @NotNull
    private String name;

    @NotNull
    private Set<StatementCreateDto> statements = new HashSet<>();
}
