package com.maemresen.fintrack.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FieldValidationErrorDto {
    private String field;
    private String message;
    private Object rejectedValue;
}
