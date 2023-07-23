package com.maemresen.fintrack.api.entity.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
public class BaseEntity {
    @Id
    private Long id;

}
