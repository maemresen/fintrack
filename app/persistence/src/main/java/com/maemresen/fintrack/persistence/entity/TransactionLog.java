package com.maemresen.fintrack.persistence.entity;

import com.maemresen.fintrack.persistence.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class TransactionLog extends BaseEntity {
    private String name;
    private String description;
    private Double amount;
    private LocalDateTime timestamp;
}
