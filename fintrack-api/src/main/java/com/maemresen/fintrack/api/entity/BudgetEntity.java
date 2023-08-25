package com.maemresen.fintrack.api.entity;

import com.maemresen.fintrack.api.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity(name = "budget")
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class BudgetEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "budget", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<StatementEntity> statements = new ArrayList<>();
}
