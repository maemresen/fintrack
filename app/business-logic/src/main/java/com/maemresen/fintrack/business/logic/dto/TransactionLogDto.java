package com.maemresen.fintrack.business.logic.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLogDto {
    private Long id;
    private String name;
    private String description;
    private Double amount;
    private LocalDateTime timestamp;
}
