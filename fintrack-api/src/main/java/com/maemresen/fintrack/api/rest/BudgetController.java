package com.maemresen.fintrack.api.rest;

import com.maemresen.fintrack.api.dto.BudgetCreateRequestDto;
import com.maemresen.fintrack.api.dto.BudgetDto;
import com.maemresen.fintrack.api.dto.StatementCreateDto;
import com.maemresen.fintrack.api.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("budget")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping("{budgetId}")
    public ResponseEntity<BudgetDto> findById(@PathVariable Long budgetId){
        return budgetService.findById(budgetId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BudgetDto>> findAll(){
        return ResponseEntity.ok(budgetService.findAll());
    }

    @PostMapping
    public ResponseEntity<BudgetDto> create(@RequestBody @Valid BudgetCreateRequestDto createRequestDto){
        return ResponseEntity.ok(budgetService.create(createRequestDto));
    }

    @PostMapping("{budgetId}/statement")
    public ResponseEntity<BudgetDto> addStatement(@PathVariable Long budgetId, @RequestBody @Valid StatementCreateDto statementCreateDto){
        return ResponseEntity.ok(budgetService.addStatement(budgetId, statementCreateDto));
    }

    @DeleteMapping("{budgetId}/statement/{statementId}")
    public ResponseEntity<Void> removeStatement(@PathVariable Long budgetId, @PathVariable Long statementId){
        budgetService.removeStatement(budgetId, statementId);
        return ResponseEntity.ok().build();
    }
}
