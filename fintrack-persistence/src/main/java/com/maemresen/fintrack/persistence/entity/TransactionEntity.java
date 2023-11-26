package com.maemresen.fintrack.persistence.entity;

import com.maemresen.fintrack.persistence.base.entity.BaseDatedEntity;
import com.maemresen.fintrack.persistence.constant.Currency;
import com.maemresen.fintrack.persistence.constant.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Entity(name = "transaction")
public class TransactionEntity extends BaseDatedEntity {

    @Column(nullable = false)
    private LocalDateTime date;

    private String description;

    private String category;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false,  name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @OneToMany(mappedBy = "transaction")
    private Set<ExchangeRateEntity> exchangeRates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionEntity that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getDate(), that.getDate()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getCategory(), that.getCategory()) && Objects.equals(getAmount(), that.getAmount()) && getCurrency() == that.getCurrency() && getTransactionType() == that.getTransactionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDate(), getDescription(), getCategory(), getAmount(), getCurrency(), getTransactionType());
    }
}
