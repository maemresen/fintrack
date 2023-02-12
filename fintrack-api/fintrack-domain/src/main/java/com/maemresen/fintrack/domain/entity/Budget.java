package com.maemresen.fintrack.domain.entity;

import com.maemresen.fintrack.domain.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Budget extends BaseEntity {

	@Column(nullable = false)
	private String name;
}
