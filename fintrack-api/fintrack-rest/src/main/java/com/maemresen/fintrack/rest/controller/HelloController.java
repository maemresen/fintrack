package com.maemresen.fintrack.rest.controller;

import com.maemresen.fintrack.rest.config.GenericResponse;
import com.maemresen.fintrack.rest.controller.model.Person;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

	@GetMapping
	public GenericResponse<String> hello() {
		return GenericResponse.ok("Hello world");
	}

	@GetMapping("/throw")
	public GenericResponse<String> throwError() {
		throw new RuntimeException("This is error with new");
	}

	@GetMapping("/invalid")
	public GenericResponse<String> throwError(@RequestBody @Valid Person person) {
		throw new RuntimeException("Hehe booy");
	}
}
