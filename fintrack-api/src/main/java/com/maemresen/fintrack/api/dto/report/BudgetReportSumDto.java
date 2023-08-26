package com.maemresen.fintrack.api.dto.report;


import com.maemresen.fintrack.api.dto.StatementDto;
import com.maemresen.fintrack.api.entity.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@FieldNameConstants
public class BudgetReportSumDto {
    private Double income;
    private Double expense;
    private Double sum;
    private Currency currency;
    private List<StatementDto> statements;
}
