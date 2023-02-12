package com.maemresen.fintrack.rest.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Person {

	@NotNull
	private String name;
}
