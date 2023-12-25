package com.maemresen.fintrack.api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/hello")
public class HelloRestController {

    @GetMapping
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok()
            .body("Hello");
    }
}
