package com.maemresen.fintrack.api.entity;

import com.maemresen.fintrack.api.entity.base.BaseDatedEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import com.maemresen.fintrack.api.entity.enums.StatementType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity(name = "statement")
public class StatementEntity extends BaseDatedEntity {

    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatementType type;

    @Column(nullable = false)
    private LocalDateTime date;

    private String category;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private BudgetEntity budget;
}
