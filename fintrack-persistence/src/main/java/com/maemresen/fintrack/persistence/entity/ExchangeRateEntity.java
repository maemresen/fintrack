package com.maemresen.fintrack.persistence.entity;


import com.maemresen.fintrack.persistence.base.entity.BaseEntity;
import com.maemresen.fintrack.persistence.constant.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity(name = "exchangeRate")
public class ExchangeRateEntity extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency base;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency target;

    @Column(nullable = false)
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private TransactionEntity transaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateEntity that)) return false;
        if (!super.equals(o)) return false;
        return getBase() == that.getBase() && getTarget() == that.getTarget() && Objects.equals(getRate(), that.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBase(), getTarget(), getRate());
    }
}
