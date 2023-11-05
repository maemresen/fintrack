package com.maemresen.fintrack.api.entity;

import com.maemresen.fintrack.api.entity.base.BaseDatedEntity;
import com.maemresen.fintrack.api.entity.base.BaseEntity;
import com.maemresen.fintrack.api.entity.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity(name = "exchange_rate")
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"base_currency", "target_currency"})
})
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class ExchangeRate extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "base_currency")
    @Enumerated(EnumType.STRING)
    private Currency baseCurrency;

    @Column(nullable = false, name = "target_currency")
    @Enumerated(EnumType.STRING)
    private Currency targetCurrency;

    @Column(nullable = false)
    private double rate;
}
