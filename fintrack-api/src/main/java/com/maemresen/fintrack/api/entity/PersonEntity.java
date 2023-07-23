package com.maemresen.fintrack.api.entity;

import com.maemresen.fintrack.api.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity(name = "person")
public class PersonEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "person")
    private Set<StatementEntity> statements;
}
